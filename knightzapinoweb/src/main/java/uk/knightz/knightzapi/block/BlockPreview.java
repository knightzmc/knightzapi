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

public class BlockPreview {
	private static final int SPLIT_BY = 20;
	private final BlockState[] original;
	private final BlockState[] changeTo;


	public BlockPreview(BlockState[] original, BlockState[] changeTo) {
		if (original.length != changeTo.length) {
			Log.warn("BlockPreview original and updated BlockState arrays are different sizes. This may be intentional" +
					", but somewhat defeats the point ofGlobal BlockPreview(s)");
		}
		this.original = original;
		this.changeTo = changeTo;
	}

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
		} else {
			Arrays.stream(change).forEach(aChange -> aChange.update(true));
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
