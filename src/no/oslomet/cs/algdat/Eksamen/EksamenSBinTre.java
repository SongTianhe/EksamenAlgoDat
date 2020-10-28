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
        //Test hvis verdi er riktig input
        Objects.requireNonNull(verdi, "Verdi kan ikke være null");

        Node<T> current = rot;
        Node<T> forelder = null; //forelder til current node
        int cmp;

        while (current != null){
            cmp=comp.compare(verdi,current.verdi);
            if(cmp == 0){
                break;
            }else{
                forelder=current;
                current = cmp<0 ? current.venstre : current.høyre;
            }
        }
        if(current == null) { //verdien finnes ikke i treet
            return false;
        }

        if(current.venstre == null && current.høyre == null && forelder != null){//hvis det er en bladnode
            if(forelder.høyre == current){
                forelder.høyre=null;
            }else {
                forelder.venstre=null;
            }
        }else if(current.venstre == null || current.høyre == null){//hvis node har bare en barn
            Node<T> barn = current.venstre != null ? current.venstre : current.høyre; //Finne ut det er venstre eller høyre barn
            if(current == rot){
                rot = barn;
                //barn.forelder = null;
            }else if(current == forelder.venstre){
                forelder.venstre = barn;
                barn.forelder = forelder;
            }
            else {
                forelder.høyre = barn;
                barn.forelder = forelder;
            }
        }else{ //node har både venstre og høyre barn
            Node<T> s = current;
            Node<T> r = current.høyre;
            while (r.venstre!=null){
                s=r; //s er forelder til r.
                r=r.venstre;
            }
            current.verdi=r.verdi; //kopi r sin verdi til current

            if(s!=current){
                s.venstre=r.høyre;
            }else{
                s.høyre=r.høyre;
            }
        }
        antall--;
        endringer++;

        return true;
    }

    public int fjernAlle(T verdi) {
        Objects.requireNonNull(verdi, "Verdi kan ikke være null");

        if(tom()){
            return 0;
        }

        int fjern = 0;

        while(inneholder(verdi)){
            fjern(verdi);
            fjern ++;
        }

        return fjern;
    }

    public int antall(T verdi) {
        int antallVerdi = 0;
        int cmp;
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
        Node<T> temp;

        while(!tom()){
            temp = rot;
            while(temp.venstre != null){
                temp = temp.venstre;
            }

            while(temp.høyre != null){
                temp = temp.høyre;
            }

            temp = null;
            antall --;
        }
    }

    private static <T> Node<T> førstePostorden(Node<T> p) {
        Objects.requireNonNull(p,"det finnes ikke rot node, treet er tom");

        //bruk en temp til å gå gjennom tre
        Node<T> temp = p;

        //løp gjennom tre ved den uendelig løkke, løkken skal sluttes nå det finnes ingen barn, dvs en bladnode
        while(true){
            if(temp.venstre != null) {
                temp = temp.venstre;
            }else if(temp.høyre != null) {
                temp = temp.høyre;
            }else{
                break;
            }
        }
        return temp;
    }

    private static <T> Node<T> nestePostorden(Node<T> p) {
        Node<T> temp = p;

        //sjekk hvis p finner forelder (hvis finner ikke, så det er rot, dvs siste i postorden)
        if(temp.forelder != null) {
            //Postorden skal print venstre subtree først, og etterpå print høyre subtree, i slutten er rot
            /*Den kan bruk førstePostorden metoden som vi har laget før,
              men den må bruk p sin forelderes høyre barn som parameter.
              Denne if sjekker hvis p er venstre barn til forelder, og forelder har høyre barn,
              så skal p vil forelderes høyre barn og kjør førstePostorden og finner neste node
            */
            if(temp == temp.forelder.venstre && temp.forelder.høyre != null){
                temp = temp.forelder.høyre;
                temp = førstePostorden(temp);
            }else{//hvis ikke, så det betyr venstre barn er ut, nå skal høyre barn ut direkt
                temp = temp.forelder;
            }
            return temp;
        }

        return null;
    }

    public void postorden(Oppgave<? super T> oppgave) {
        //Finn den første noden i postorden ved bruk av førstePostorden() metode
        Node<T> node = førstePostorden(rot);

        while(node != null){
            //skrive ut noden
            oppgave.utførOppgave(node.verdi);
            //flytt node til nest node i postorden
            node = nestePostorden(node);
        }
    }

    public void postordenRecursive(Oppgave<? super T> oppgave) {
        postordenRecursive(rot, oppgave);
    }

    private void postordenRecursive(Node<T> p, Oppgave<? super T> oppgave) {
        //hvis noden er ikke null
        if(p != null){
            postordenRecursive(p.venstre,oppgave);//går venstre til det finnes ikke noe venstre barn
            postordenRecursive(p.høyre,oppgave);//går høyre til det finnes ikke noe høyre
            // barn

            //hvis det er ingen venstre og høyre barn,skrive ut noden
            oppgave.utførOppgave(p.verdi);
        }
    }

    public ArrayList<T> serialize() {
        ArrayList<T> nodeList = new ArrayList();
        List<Node> hjelpList=new ArrayList();

        //hvis tree er tom
        if(tom()){
            return nodeList;
        }

        //legge rot til index 0 av hjelpList
        Node<T> node = rot;
        hjelpList.add(node);

        //i er index av hjelpList, loop stopes når tree er ferdig
        //det traverserer treet i nivå orden
        for(int i=0; antall>i; i++){
            //hent node informasjon fra hjelpList
            node = hjelpList.get(i);

            if(node.venstre != null){
                //lagre node i hjelpList, slik at kan finner det etterpå
                hjelpList.add(node.venstre);
            }
            if(node.høyre != null){
                hjelpList.add(node.høyre);
            }
            //legger verdi til nodeList
            nodeList.add(node.verdi);
        }
        return nodeList;
    }

    static <K> EksamenSBinTre<K> deserialize(ArrayList<K> data, Comparator<? super K> c) {
        //sjekk hvis list som kom inn er ikke tom
        if(data.isEmpty()){
            return null;
        }

        //instanser tree
        EksamenSBinTre<K> deserializeTre = new EksamenSBinTre<>(c);

        for(K verdi : data){
            deserializeTre.leggInn(verdi);
        }

        return deserializeTre;
    }

    public static void main(String[] args) {
        int[] a = {4,7,2,9,4,10,8,7,4,6,1};
        int[] b = {1, 4, 1, 3, 1, 2, 1, 1};
        EksamenSBinTre<Integer> tre = new EksamenSBinTre<>(Comparator.naturalOrder());
        for(int verdi : b){
            tre.leggInn(verdi);
        }

        //tre.nullstill();
        System.out.println("antall verdi : "+tre.fjernAlle(1));
        System.out.println("antall : "+tre.antall());
        System.out.println(tre.toStringPostOrder());

        /*
        System.out.println(tre.fjernAlle(4)); // 3
        System.out.println(tre.toStringPostOrder());

        System.out.println(tre.fjernAlle(7));
        System.out.println(tre.toStringPostOrder());

        tre.fjern(8);
        System.out.println(tre.antall()); // 5
        System.out.println(tre.toStringPostOrder());*/

    }


} // ObligSBinTre
