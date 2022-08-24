package es.uned.lsi.eped.pract2021_2022;

import es.uned.lsi.eped.DataStructures.IteratorIF;
import es.uned.lsi.eped.DataStructures.SequenceIF;

/* Representa un array disperso en el que los elementos se
 * indexan bajo un indice entero y no se reserva memoria
 * para un elemento si no se ha usado su indice
 */
public interface SparseArrayIF<E> extends SequenceIF<E> {

    /* Indexa el elemento elem bajo el indice pos.
     * Si ya habia un elemento bajo el mismo indice, el nuevo
     * elemento substituye al anterior.
     */
	public void set(int pos,E elem);
	
	/* Devuelve el elemento indexado bajo el indice pos.
	 * Si no existe un elemento indexado bajo el indice pos,
	 * devuelve null.
	 */
	public E get(int pos);
	
	/* Elimina el elemento indexado bajo el indice pos.
	 * Elimina toda la memoria utilizada para almacenar el elemento
	 * borrado.
	 * Si no existe un elemento indexado bajo el indice pos,
	 * esta operacion no realiza ninguna modificacion en la estructura.
	 */
	public void delete(int pos);
	
	/* Devuelve un iterador de todos los indices utilizados
	 * en el array disperso, por orden creciente de indice.
	 */
	public IteratorIF<Integer> indexIterator();
}
