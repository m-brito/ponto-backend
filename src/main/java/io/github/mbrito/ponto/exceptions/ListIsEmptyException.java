package io.github.mbrito.ponto.exceptions;

public class ListIsEmptyException extends Exception {
	private static final long serialVersionUID = 1L;

	private String listName;

	public ListIsEmptyException(String listName) {
		super(String.format("%s não pode estar vazia para esta requisição", listName));
    }

	public String getListName() {
		return listName;
	}

	public void setListName(String listName) {
		this.listName = listName;
	}
}
