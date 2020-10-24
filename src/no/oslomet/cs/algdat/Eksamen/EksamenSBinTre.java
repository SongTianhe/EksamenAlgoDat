package no.oslomet.cs.algdat.Eksamen;


import java.util.*;

public class EksamenSBinTre<T> {
    private static final class Node<T>   // en indre nodeklasse
    {
        private T verdi;                   // nodens verdi
        private Node<T> venstre, høyre;    // venstre og høyre barn
        private Node<T> forelder;          // forelder

        // konstruktør
        private Node(T verdi, Node<T> v, Node<T> h, Node<T> forelder) {
            this.verdi = verdi;
            venstre = v;
            høyre = h;
            this.forelder = forelder;
        }

        private Node(T verdi, Node<T> forelder)  // konstruktør
        {
            this(verdi, null, null, forelder);
        }

        @Override
        public String toString() {
            return "" + verdi;
        }

    } // class Node

    private Node<T> rot;                            // peker til rotnoden
    private int antall;                             // antall noder
    private int endringer;                          // antall endringer

    private final Comparator<? super T> comp;       // komparator

    public EksamenSBinTre(Comparator<? super T> c)    // konstruktør
    {
        rot = null;
        antall = 0;
        comp = c;
    }

    public boolean inneholder(T verdi) {
        if (verdi == null) return false;

        Node<T> p = rot;

        while (p != null) {
            int cmp = comp.compare(verdi, p.verdi);
            if (cmp < 0) p = p.venstre;
            else if (cmp > 0) p = p.høyre;
            else return true;
        }

        return false;
    }

    public int antall() {
        return antall;
    }

    public String toStringPostOrder() {
        if (tom()) return "[]";

        StringJoiner s = new StringJoiner(", ", "[", "]");

        Node<T> p = førstePostorden(rot); // går til den første i postorden
        while (p != null) {
            s.add(p.verdi.toString());
            p = nestePostorden(p);
        }

        return s.toString();
    }

    public boolean tom() {
        return antall == 0;
    }

    public boolean leggInn(T verdi) {
        //Test hvis verdi er riktig input
        Objects.requireNonNull(verdi, "Verdi kan ikke være null");

        Node<T> currentNode = rot;
        Node<T> temp = null;
        int cmp = 0;

        //gå gjennom tree, sluttes når det finner passet posisjon for nye node
        while(currentNode != null){
            temp = currentNode;
            //cmp er -1 hvis verdi er mindre enn currentNode sin verdi
            //cmp er 1 hvis verdi er større enn currentNode sin verdi
            cmp = comp.compare(verdi,currentNode.verdi);
            currentNode = cmp<0 ? currentNode.venstre : currentNode.høyre;
        }

        //Nå er currentNode fant riktig plass, og temp er foreldre til currentNode
        //Opprett en nye Node
        currentNode = new Node<>(verdi,temp);

        if(temp == null){//hvis tree er tom, legge nye node som rot
            rot = currentNode;
        }else if (cmp<0){//cmp er saved i forrige while-løkk, hvis cmp=-1, så betyr currentNode er mindre enn temp, og er nå venstreBarn til temp.
            temp.venstre=currentNode;
        }else {//hvis cmp=1, så betyr currentNode er større enn temp, og er nå høyreBarn til temp.
            temp.høyre=currentNode;
        }

        //antall og endring øker
        antall++;
        endringer++;

        return true;
    }

    public boolean fjern(T verdi) {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public int fjernAlle(T verdi) {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public int antall(T verdi) {
        int antallVerdi = 0;
        int cmp = 0;
        Node<T> temp = rot;

        while(temp != null) {
            cmp = comp.compare(verdi,temp.verdi);

            //hvis cmp=0, betyr verdi og temp sin verdi er sammen
            if(cmp == 0) {
                antallVerdi ++;
                //gå videre med tre, ved flytt temp til høyre barn, fordi det skal legges høyre hvis det er sammen tall
                temp = temp.høyre;
            }else{//fortsett hvis det er ikke sammen verdi
                temp = cmp<0 ? temp.venstre : temp.høyre;
            }
        }

        return antallVerdi;
    }

    public void nullstill() {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    private static <T> Node<T> førstePostorden(Node<T> p) {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    private static <T> Node<T> nestePostorden(Node<T> p) {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public void postorden(Oppgave<? super T> oppgave) {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public void postordenRecursive(Oppgave<? super T> oppgave) {
        postordenRecursive(rot, oppgave);
    }

    private void postordenRecursive(Node<T> p, Oppgave<? super T> oppgave) {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public ArrayList<T> serialize() {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    static <K> EksamenSBinTre<K> deserialize(ArrayList<K> data, Comparator<? super K> c) {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public static void main(String[] args) {
        Integer[] a = {4,7,2,9,4,10,8,7,4,6};
        EksamenSBinTre<Integer> tre = new EksamenSBinTre<>(Comparator.naturalOrder());
        for(int verdi : a){
            tre.leggInn(verdi);
        }

        System.out.println(tre.antall());
        System.out.println(tre.antall(5));
        System.out.println(tre.antall(4));
        System.out.println(tre.antall(7));
        System.out.println(tre.antall(10));
    }


} // ObligSBinTre
