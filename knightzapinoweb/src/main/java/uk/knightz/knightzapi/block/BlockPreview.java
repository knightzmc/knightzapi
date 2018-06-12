package uk.knightz.knightzapi.block;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.block.BlockState;
import uk.knightz.knightzapi.KnightzAPI;
import uk.knightz.knightzapi.lang.Log;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * This class was created by AlexL (Knightz) on 31/01/2018 at 21:04.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 * <p>
 * Represents an undoable action of changing one or many blocks. Typically it is used for small amounts of blocks, but a task splitting
 * place is in system if not
 **/
public class BlockPreview {
	private static final int SPLIT_BY = 20;
	private final BlockState[] original;
	private final BlockState[] changeTo;


	public BlockPreview(BlockState[] original, BlockState[] changeTo) {
		if (original.length != changeTo.length) {
			Log.warn("BlockPreview original and updated BlockState arrays are different sizes. This may be intentional" +
					", but somewhat defeats the point of BlockPreview(s)");
		}
		this.original = original;
		this.changeTo = changeTo;
	}

	public BlockPreview(Function<BlockState, BlockState> change, BlockState... original) {
		this.original = original;
		BlockState[] temp = new BlockState[original.length];
		int i = 0;
		for (BlockState b : original) {
			temp[i] = change.apply(b);
			i++;
		}
		this.changeTo = temp;
	}

	public void apply() {
		change(changeTo);
	}

	public void undo() {
		change(original);
	}

	private void change(BlockState[] change) {
		if (change.length >= SPLIT_BY) {
			split(new SplitTask() {
				@Override
				void run() {
					for (int i = 0; i < change.length; i++) {
						if (i % SPLIT_BY == 0) {
							split();
						}
						int finalI = i;
						runSync(() -> change[finalI].update(true));
					}
				}
			});
		}
	}

	private void split(SplitTask task) {
		Bukkit.getScheduler().runTaskAsynchronously(KnightzAPI.getP(), task::run);
	}


	private abstract class SplitTask {

		/**
		 * Will always be called asynchronously
		 */
		protected void split() {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}


		/**
		 * Will always be called asynchronously,call {@link this#runSync(Runnable)} to run a task synchronously
		 */
		abstract void run();


		protected void runSync(Runnable runnable) {
			Validate.notNull(runnable);
			Bukkit.getScheduler().runTask(KnightzAPI.getP(), runnable);
		}
	}
}
