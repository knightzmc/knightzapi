package uk.knightz.knightzapi.lang.placeholder;

import lombok.Data;

@Data
public class ObjectPlaceholder<T> {
	protected final String placeholder;

	public ObjectPlaceholder(String placeholder, T replaceWith) {
		this.placeholder = placeholder;
		this.replaceWith = replaceWith;
	}

	protected final T replaceWith;
}
