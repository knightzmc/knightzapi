package uk.knightz.knightzapi.utils;

import java.util.function.Consumer;
import java.util.function.Function;

public class Functions {
	private Functions() {
	}


	public static Runnable emptyRunnable() {
		return () -> {
		};
	}

	public static <T> Consumer<T> emptyConsumer() {
		return t -> {
		};
	}

	public static <F, A> Function<F, A> emptyFunction(A toReturn) {
		return f -> toReturn;
	}
}
