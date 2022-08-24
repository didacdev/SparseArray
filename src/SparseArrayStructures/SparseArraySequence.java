package es.uned.lsi.eped.pract2021_2022;

import es.uned.lsi.eped.DataStructures.IteratorIF;
import es.uned.lsi.eped.DataStructures.Sequence;
import es.uned.lsi.eped.DataStructures.List;

public class SparseArraySequence<E> extends Sequence<E> implements SparseArrayIF<E> {
	
//Clases privadas para implementar los iteradores
	
	//Clase para instanciar un iterador que itere sobre los índices almacenados en el array
	private class IndexIterator implements IteratorIF<Integer>{
		
		private IteratorIF<IndexedPair<E>> iterator;
		
		IndexIterator(){
			iterator = sequence.iterator();
		}
		
		public Integer getNext() {
			IndexedPair<E> nodo = iterator.getNext();
			return nodo.getIndex();
		}
		
		public boolean hasNext() {
			return iterator.hasNext();
		}
		
		public void reset() {
			iterator.reset();
		}
	}
	
	//Clase para instanciar un iterador que itere sobre los elementos almacenados en el array
	private class ElementIterator implements IteratorIF<E>{
		
		private IteratorIF<IndexedPair<E>> iterator;
		
		ElementIterator(){
			iterator = sequence.iterator();
		}
		
		public E getNext() {
			IndexedPair<E> nodo = iterator.getNext();
			return nodo.getValue();
		}
		
		public boolean hasNext() {
			return iterator.hasNext();
		}
		
		public void reset() {
			iterator.reset();
		}
		
	}
	

//Atributos y constructores
	protected List<IndexedPair<E>> sequence;
	
	public SparseArraySequence() {
		super();
		sequence = new List<>();
	}
	
	public SparseArraySequence(SparseArraySequence<E> e) {
		super(e);
		sequence = e.sequence;
	}
	
//Metodos heredados de sequence que son implementados para el SparseArray
	
	/**
	 * Elimina todo el contenido del array
	 */
	public void clear() {
		super.clear();
		sequence.clear();
	}
	
	/**
	 * Comprueba si el array contiene un elemento igual al introducido como parámetro
	 * @param e: elemento que se quiere comprobar su existencia
	 * @return true si el elemento se encuentra en el array
	 */
	public boolean contains(E e) {
		IteratorIF<IndexedPair<E>> iterator = sequence.iterator();
		
		while(iterator.hasNext()) {
			IndexedPair<E> node = iterator.getNext();
			if(node.getValue().equals(e)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Iterador de elementos
	 */
	public IteratorIF<E> iterator(){
		return new ElementIterator();
	}
	
	
//Métodos privados de SparseArraySequence
	
	/**
	 * Comprueba si existe un elemento en el array con un índice similar al introducido como parámetro
	 * @param index
	 * @return true si existe un elemento con el mismo índice que el parámetro
	 */
	private boolean containsIndex(int index) {
		
		IteratorIF<Integer> iterator = indexIterator();
		
		while(iterator.hasNext()){
			
			int n = iterator.getNext();
			if(n == index) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Devuelve la posición dentro del atributo sequence que ocupa el nodo que contiene el índice index
	 * @param index: indice del elemento del que se quiere comprobar su posición
	 * @return la posición del nodo en el atributo sequence
	 */
	private int getNodePosition(int index) {
		int pos = 0;
		IteratorIF<Integer> iterator = indexIterator();
		
		while(iterator.hasNext()){
			if(iterator.getNext() <= index){
				pos++;
			}else{
				break;
			}
		}
		return pos;
	}
	
	/**
	 * Devuelve la posición del nodo previo al nodo que se encuentra en la posición index dentro del atributo sequence
	 * @param index: posición del elemento del cual se quiere conocer la posición del nodo previo
	 * @return la posición del nodo previo o 0 si no existe
	 */
	private int getPreviousNodePosition(int index) {
		
		int previousPosition = 0;
		IteratorIF<Integer> iterator = indexIterator();
		
		while(iterator.hasNext()){
			if(iterator.getNext() < index){
				previousPosition++;
			}else{
				break;
			}
		}
		
		return previousPosition;
	}
	
	
//Métodos públicos de SparseArray
	
	/* Indexa el elemento elem bajo el indice pos.
     * Si ya habia un elemento bajo el mismo indice, el nuevo
     * elemento substituye al anterior.
     */
	public void set(int pos,E elem) {
		
		IndexedPair<E> newNode = new IndexedPair<>(pos, elem);
		//Comprueba si el nodo ya existe
		if(containsIndex(pos)) {
			
			sequence.set(getNodePosition(pos), newNode);
			
		}
		// Crea el nodo con los atributos introducidos como parámetros
		else {
			
			int position = 1;
			
			position = getPreviousNodePosition(pos) + 1;
			sequence.insert(position, newNode);
			
			this.size++;
		}
		
	}
	
	/* Devuelve el elemento indexado bajo el indice pos.
	 * Si no existe un elemento indexado bajo el indice pos,
	 * devuelve null.
	 */
	public E get(int pos) {
		
		IteratorIF<IndexedPair<E>> iterator = sequence.iterator();
		
		while(iterator.hasNext()) {
			IndexedPair<E> nodo = iterator.getNext();
			if(nodo.getIndex()  == pos) {
				return nodo.getValue();
			}
		}
		return null;
		
	}
	
	/* Elimina el elemento indexado bajo el indice pos.
	 * Elimina toda la memoria utilizada para almacenar el elemento
	 * borrado.
	 * Si no existe un elemento indexado bajo el indice pos,
	 * esta operacion no realiza ninguna modificacion en la estructura.
	 */
	public void delete(int pos) {
		
		if(containsIndex(pos)) {
			
			int position = getNodePosition(pos);
			
			sequence.remove(position);

			this.size--;
			
		}
		
	}
	
	/* Devuelve un iterador de todos los indices utilizados
	 * en el array disperso, por orden creciente de indice.
	 */
	public IteratorIF<Integer> indexIterator(){return new IndexIterator();}
	
	
	

}