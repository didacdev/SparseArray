package es.uned.lsi.eped.pract2021_2022;

/* Representa un par indexado */
public class IndexedPair<E> {

	private int index;
	private E value;
	
	/* Constructor */
	public IndexedPair(int index,E value) {
		this.index = index;
		this.value = value;
	}
	
	/* Obtiene el indice */
	public int getIndex() {
		return this.index;
	}
	
	/* Obtiene el valor */
	public E getValue() {
		return this.value;
	}
	
	/* Modifica el valor */
	public void setValue(E value) {
		this.value = value;
	}
}
