package es.uned.lsi.eped.pract2021_2022;

import es.uned.lsi.eped.DataStructures.BTreeIF;
import es.uned.lsi.eped.DataStructures.IteratorIF;
import es.uned.lsi.eped.DataStructures.Stack;
import es.uned.lsi.eped.DataStructures.StackIF;
import es.uned.lsi.eped.DataStructures.Sequence;
import es.uned.lsi.eped.DataStructures.BTree;


public class SparseArrayBTree<E> extends Sequence<E> implements SparseArrayIF<E> {
// Clases privadas para implementar los iteradores
	
	/**
	 * Clase para instanciar un iterador por índices
	 */
	private class IndexIterator implements IteratorIF<Integer>{
		
		private IteratorIF<IndexedPair<E>> iterator;
		
		Object mode;
		
		IndexIterator(){
			mode = BTreeIF.IteratorModes.BREADTH;
			iterator = btree.iterator(mode);
		}
		
		public Integer getNext() {
			IndexedPair<E> nodo = iterator.getNext();
			while(nodo == null && iterator.hasNext()) {
				nodo = iterator.getNext();
			}
			return nodo.getIndex();
		}
		
		public boolean hasNext() {
			return iterator.hasNext();
		}
		
		public void reset() {
			iterator.reset();
		}
	}

	/**
	 * Clase para instanciar un iterador por elementos
	 */
	private class ElementIterator implements IteratorIF<E>{
		
		private IteratorIF<IndexedPair<E>> iterator;
		
		Object mode;
		
		ElementIterator(){
			mode = BTreeIF.IteratorModes.BREADTH;
			iterator = btree.iterator(mode);
		}
		
		public E getNext() {
			IndexedPair<E> nodo = iterator.getNext();
			while(nodo == null && iterator.hasNext()) {
				nodo = iterator.getNext();
			}
			return nodo.getValue();
		}
		
		public boolean hasNext() {
			return iterator.hasNext();
		}
		
		public void reset() {
			iterator.reset();
		}
		
	}
	
	
// Atributos y constructor
	
	protected BTreeIF<IndexedPair<E>> btree;
	
	/**
	 * Constructores de SparseArrayBTree
	 */
	public SparseArrayBTree() {
		super();
		btree = new BTree<>(); //inicia la variable btree creando un nuevo BTree en ella
	}
	
	public SparseArrayBTree(SparseArrayBTree<E> e) {
		super(e);
		btree = e.btree;
	}
	
// Metodos privados del array disperso	
	
	/**
	 * Devuelve la posición traducida al sistema binario
	 * @param n: posición que se desea traducir
	 * @return un stack con la posición en binario a base de elementos booleanos
	 */
	private StackIF<Boolean> num2bin(int n) {
		Stack<Boolean> salida = new Stack<Boolean>();
		if ( n == 0 ) {
			salida.push(false);
		} else {
			while ( n != 0 ) {
				salida.push((n % 2) == 1);
				n = n / 2;
			}
		}
		return salida;
	}
	
	/**
	 * Comprueba si existe un nodo en la estructura en la posición pos
	 * @param pos: posición que se desea examinar
	 * @return true si existe un nodo en dicha posición
	 */
	private Boolean existsIndex(int pos){

		BTreeIF<IndexedPair<E>> node = getChild(pos);

		if(node != null){
			if(node.getRoot() != null){
				if(node.getRoot().getIndex() == pos){
					return true;
				}else{
					return false;
				}
			}else{
				return false;
			}
		}else{
			return false;
		}

	}
	

	/**
	 * Devuelve el nodo padre del nodo que se encuentra en la posición binaria introducida
	 * @param posBinaria: posición traducida al sistema binario
	 * @param btree: nodo a partir del cual comienza la búsqueda
	 * @return nodo: el nodo padre al nodo que se encuentra en la posición posBinaria
	 */
	private BTreeIF<IndexedPair<E>> getFather(StackIF<Boolean> posBinaria, BTreeIF<IndexedPair<E>> btree) {
		
		BTreeIF<IndexedPair<E>> nodo = null;
		//Caso recursivo
		if(posBinaria.size() > 1) {
			if(posBinaria.getTop()) {
				posBinaria.pop();
				nodo = getFather(posBinaria, btree.getRightChild());	
			}else {
				posBinaria.pop();
				nodo = getFather(posBinaria, btree.getLeftChild());
			}
		}
		//Caso no recursivo
		else {
			nodo =  btree;
		}
		
		return nodo;
	}

	/**
	 * Elimina todos los nodos que se quedan vacíos tras la eliminación de un nodo
	 * @param pos: posición a partir de la cual se quiere iniciar la limpieza
	 */
	private void cleanBTree(int pos){
		StackIF<Boolean> posBinaria = num2bin(pos);
		BTreeIF<IndexedPair<E>> nodo = btree;
		Stack<BTreeIF<IndexedPair<E>>> nodosRecorridos = new Stack<>();
		 
		while(!posBinaria.isEmpty()){
			nodosRecorridos.push(nodo);
			if(!nodo.isLeaf()){
				if(posBinaria.getTop()){
					nodo = nodo.getRightChild();
				}else{
					nodo = nodo.getLeftChild();
				}
			}
			posBinaria.pop();
     	}

		while(!nodosRecorridos.isEmpty()){
			nodo = nodosRecorridos.getTop();
			//El nodo que se revisa no tiene hijos
			if(nodo.getNumChildren() == 0){
				if(nodo.getRoot() == null){
					nodo.clear();
				}else{
					nodo.removeRightChild();
					nodo.removeLeftChild();;
				}
			}
			//El nodo que se revisa tiene un hijo
			else if(nodo.getNumChildren() == 1){
				if(nodo.getRoot() == null){
					//Se comprueba si el hijo derecho existe
					if(nodo.getRightChild() != null){
						if(nodo.getRightChild().isEmpty() && nodo.getRightChild().getNumChildren() == 0){
							nodo.clear();
						}
					} 
					//Se comprueba si el hijo izquierdo existe
					else if(nodo.getLeftChild() != null){
						if(nodo.getLeftChild().isEmpty() && nodo.getLeftChild().getNumChildren() == 0){
							nodo.clear();
						}
					}
				}else{
					//Se comprueba si el hijo derecho existe
					if(nodo.getRightChild() != null){
						if(nodo.getRightChild().isEmpty() && nodo.getRightChild().getNumChildren() == 0){
							nodo.removeRightChild();;
						}
					} 
					//Se comprueba si el hijo izquierdo existe
					else if(nodo.getLeftChild() != null){
						if(nodo.getLeftChild().isEmpty() && nodo.getLeftChild().getNumChildren() == 0){
							nodo.removeLeftChild();;
						}
					}
				}
			}
			//El nodo que se revisa tiene dos hijos
			else if(nodo.getNumChildren() == 2){
				//Comprueba si existe el hijo derecho
				if(nodo.getRightChild() != null){
					if(nodo.getRightChild().isEmpty() && nodo.getRightChild().getNumChildren() == 0){
						nodo.removeRightChild();
					}
				} 
				//Comprueba si existe el hijo izquierdo
				if(nodo.getLeftChild() != null){ 
					if(nodo.getLeftChild().isEmpty() && nodo.getLeftChild().getNumChildren() == 0){
						nodo.removeLeftChild();
					}
				}
				//Si la root del nodo no es null y se han eliminado los dos hijos
				if(nodo.getRoot() == null && nodo.getNumChildren() == 0){
					nodo.clear();
				}
			}
			nodosRecorridos.pop();
		}
	}
	
	/**
	 * Devuelve el nodo con el índice introducido como parámetro
	 * @param pos: posición que se desea examinar
	 * @return el nodo que tiene como índice pos o null si el nodo no existe
	 */
	private BTreeIF<IndexedPair<E>> getChild(int pos){
		BTreeIF<IndexedPair<E>> node = btree;
		boolean bit = ((pos % 2) == 1);
		//Caso no recursivo
		if(pos < 2){
			if(bit){
				node = node.getRightChild();  
			}else{
				node = node.getLeftChild();
			}
		}
		//Caso recursivo
		else{
			pos = pos / 2;
			node = getChild(pos);
			if(node != null){
				if(bit){
					if(node.getRightChild() != null){
						node = node.getRightChild();
					}else{
						node = null;
					}
				}else{
					if(node.getLeftChild() != null){
						node = node.getLeftChild();
					}else{
						node = null;
					}
				}
			}
		}
		return node;
	}

	
	/**
	 * Devuelve el nodo con el índice introducido como parámetro y crea el camino para llevar al nodo
	 * @param pos: posición en la que se encuentra el nodo
	 * @return el nodo que tiene como índice pos
	 */
	private BTreeIF<IndexedPair<E>> setTree(int pos){
		BTreeIF<IndexedPair<E>> node = btree;
		boolean bit = ((pos % 2) == 1);
		//Caso no recursivo
		if(pos < 2){
			if(bit){
                if(node.getRightChild() == null){
					node.setRightChild(new BTree<>());
                    node = node.getRightChild();
                }else{
                    node = node.getRightChild();
                }
			}else{
                if(node.getLeftChild() == null){
					node.setLeftChild(new BTree<>());
                    node = node.getLeftChild();
                }else{
                    node = node.getLeftChild();
                }
			}
		}
		//Caso recursivo
		else{
			pos = pos / 2;
			node = setTree(pos);
			if(bit){
                if(node.getRightChild() == null){
					node.setRightChild(new BTree<>());
                    node = node.getRightChild();
                }else{
                    node = node.getRightChild();
                }
			}else{
                if(node.getLeftChild() == null){
					node.setLeftChild(new BTree<>());
                    node = node.getLeftChild();
                }else{
                    node = node.getLeftChild();
                }
			}
		}

		return node;
	}

// Metodos publicos del array disperso
	
	/* Indexa el elemento elem bajo el indice pos.
     * Si ya habia un elemento bajo el mismo indice, el nuevo
     * elemento substituye al anterior.
     */
	public void set(int pos,E elem) {
		
		//Examina si existe un nodo en la posición pos 
		if(!existsIndex(pos)){
			this.size++; //el tamaño crece en 1 si el nodo que se introduce es nuevo
		}
        BTreeIF<IndexedPair<E>> node = setTree(pos);
		node.setRoot(new IndexedPair<>(pos, elem));

	}
	
	/* Devuelve el elemento indexado bajo el indice pos.
	 * Si no existe un elemento indexado bajo el indice pos,
	 * devuelve null.
	 */
	public E get(int pos) {
		
		//se examina si existe el nodo que se desea obtener
		if(existsIndex(pos)){
			BTreeIF<IndexedPair<E>> node = getChild(pos);
			return node.getRoot().getValue();
		}else{
			return null;
		}
		
	}
	
	/* Elimina el elemento indexado bajo el indice pos.
	 * Elimina toda la memoria utilizada para almacenar el elemento
	 * borrado.
	 * Si no existe un elemento indexado bajo el indice pos,
	 * esta operacion no realiza ninguna modificacion en la estructura.
	 */
	public void delete(int pos) {
		//Examina si el nodo que se desea eliminar existe
		if(existsIndex(pos)) {

			BTreeIF<IndexedPair<E>> node = getFather(num2bin(pos), btree);
			
			//Se recorre la estructura en busca del nodo que se desea eliminar
			if(node.getRightChild() != null && node.getRightChild().equals(getChild(pos))){
				if(node.getRightChild().isLeaf()){
					node.removeRightChild();
					cleanBTree(pos); //se limipia la estructura de nodos residuales
				}else{
					node.getRightChild().setRoot(null);
				}
			}else if(node.getLeftChild() != null && node.getLeftChild().equals(getChild(pos))){
				if(node.getLeftChild().isLeaf()){
					node.removeLeftChild();
					cleanBTree(pos);
				}else{
					node.getLeftChild().setRoot(null);
				}
			}
			//Si el array queda vacío tras la eliminación se limpia entero para que su tamaño sea 0
			node = btree;
			if(btree.getNumChildren() == 0){
				btree.clear();
			}
			this.size--;
		}
	}
	
	/* Devuelve un iterador de todos los indices utilizados
	 * en el array disperso, por orden creciente de indice.
	 */
	public IteratorIF<Integer> indexIterator(){
		
		return new IndexIterator();
		
	}

//Reescritura de metodos heredados de sequence
	
	/* Elimina todo el contenido del array
	 */
	public void clear() {
		super.clear();
		btree.clear();
	}
	
	/* Devuelve true si el elemento introducido como parametro se encuentra en el array
	 */
	public boolean contains(E e) {
		IteratorIF<E> iterator = iterator();
		
		while(iterator.hasNext()) {
			if(iterator.getNext().equals(e)) {
				return true;
			}
		}
		return false;
	}
	
	/*Iterador lineal sobre los elementos del array
	 */
	public IteratorIF<E> iterator() {
		// TODO Auto-generated method stub
		return new ElementIterator();
	}

}
