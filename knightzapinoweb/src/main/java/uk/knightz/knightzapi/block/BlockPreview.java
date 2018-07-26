/*
 * MIT License
 *
 * Copyright (c) 2018 Alexander Leslie John Wood
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package uk.knightz.knightzapi.block;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.block.BlockState;
import uk.knightz.knightzapi.KnightzAPI;
import uk.knightz.knightzapi.lang.Log;

import java.util.Arrays;
import java.util.function.Function;

/**
 * A collection of Block changes that can be applied and reversed for previewing Block changing operations.
 * <p>
 * Example Usage can be found on the GitHub Wiki.
 */
public class BlockPreview {
	private static final int SPLIT_BY = 0x13;
	private final BlockState[] original;
	private final BlockState[] changeTo;


	/**
	 * @param original The original group of BlockState objects that will be changed.
	 * @param changeTo The changed group of BlockState objects that will replace the original BlockStates.
	 */
	public BlockPreview(BlockState[] original, BlockState[] changeTo) {
		if (original.length != changeTo.length) {
			Log.warn("BlockPreview original and updated BlockState arrays are different sizes. This may be intentional" +
					", but somewhat defeats the point of BlockPreview(s)");
		}
		this.original = original;
		this.changeTo = changeTo;
	}

	/**
	 * @param change A function that will be applied on each original BlockState in order to set it to its new state.
	 * @param original The original group of BlockState objects that will be changed to the result of the given Function.
	 */
	public BlockPreview(Function<BlockState, BlockState> change, BlockState... original) {
		this.original = original;
		BlockState[] temp = new BlockState[original.length];
		int i = 0;
		for (BlockState b : original) {
			temp[i] = change.apply(b.getBlock().getState());
			i++;
		}
		undo();
		this.changeTo = temp;
	}

	/**
	 * Apply this preview, changing all original blocks to the replacement blocks.
	 */
	public void apply() {
		change(changeTo);
	}

	/**
	 * Undo the preview, changing all replaced blocks to their original form.
	 */
	public void undo() {
		change(original);
	}

	/**
	 * Changes all current blocks to the given array of BlockState objects.
	 * Usually shouldn't be used, for internals only
	 * @param change An array of BlockState objects that will be applied
	 */
	private void change(BlockState[] change) {
		if (change.length >= SPLIT_BY) {
			split(new SplitTask() {
				@Override
				void run() {
					for (int i = 0; i < change.length; i++) {
						if ((i & SPLIT_BY) == 0) {
							split();
						}
						int finalI = i;
						runSync(() -> change[finalI].update(true));
					}
				}
			});
		} else {
			Arrays.stream(change).forEach(aChange -> aChange.update(true));
		}
	}

	private void split(SplitTask task) {
		Bukkit.getScheduler().runTaskAsynchronously(KnightzAPI.getP(), task::run);
	}

	/**
	 * A class that handles splitting heavy block operations into clusters, running every 0.5 seconds
	 */
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
		/**
		 * Schedule a given task on the main Thread
		 * @param runnable The runnable to schedule to run on the main Thread
		 */
		protected void runSync(Runnable runnable) {
			Validate.notNull(runnable);
			Bukkit.getScheduler().runTask(KnightzAPI.getP(), runnable);
		}
	}
}
