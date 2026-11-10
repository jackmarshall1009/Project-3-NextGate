
public class Queue<T> {
    private class Node<T> {
        private T data;
        private Node<T> next;

        public Node(T data){
            this.data = data;
            this.next = null;
        }

        public T getData(){
            return data;
        }

        public Node<T> getNext(){
            return next;
        }

        public void setNext(Node<T> next){
            this.next = next;
        }

    }


    private Node<T> firstNode;
    private Node<T> lastNode;
    private int size;

    public Queue(){
        firstNode = null;
        lastNode = null;
        size = 0;
    }

    public T peek() {
        if (isEmpty()) {
            return null;
        }
        return firstNode.getData();
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void enqueue(T newEntry) {
        Node<T> newNode = new Node<>(newEntry);
        if (isEmpty()) {
            firstNode = newNode;
            lastNode = newNode;
        }
        else{
            lastNode.setNext(newNode);
            lastNode = newNode;
        }
        size++;
    }

    public T dequeue(){
        if(isEmpty()){
            return null;
        }

        T front = firstNode.getData();
        firstNode = firstNode.getNext();

        if(firstNode == null){
            lastNode = null;
        }
        size--;
        return front;
    }

    public int size(){
        return size;
    }

    public int positionOf(T entry){
        Node<T> curr = firstNode;
        int position = 1;
        while(curr != null){
            if(curr.getData().equals(entry)){
                return position;
            }
            curr = curr.getNext();
            position++;
        }

        return -1;
    }

    public boolean remove(T entry) {
        Node<T> curr = firstNode;
        Node<T> prev = null;

        while (curr != null) {
            if (curr.getData().equals(entry)) {
                if (prev == null) {
                    firstNode = curr.getNext();
                }
                else {
                    prev.setNext(curr.getNext());
                }

                if (curr == lastNode) {
                    lastNode = prev;
                }

                size--;
                return true;
            }

            prev = curr;
            curr = curr.getNext();
        }

        return false;
    }

    public Object[] toArray(){
        Object[] array = new Object[size];
        Node<T> curr = firstNode;
        int i = 0;
        while(curr != null){
            array[i] = curr.getData();
            curr = curr.getNext();
            i++;
        }
        return array;
    }

}
