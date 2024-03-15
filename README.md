------------- TEMA 2 -------------
- ALGORITMI PARALELI SI DISTRIBUITI -


--------------------------------------------
| Student: Bogdan Alexandra-Lacramioara    |  
| Grupa: 334CD                             |
--------------------------------------------


Implementare MyDispatcher
---------------------------------------------

    Clasa MyDispatcher este concepută pentru a distribui eficient sarcini către o colecție de noduri de calcul, bazate pe diverse algoritme de programare. Fiecare nod de calcul este reprezentat de clasa Host.
    
    Inițializare
    -------------------------
    - Round Robin: Sarcinile sunt asignate gazdelor într-un mod circular. Indexul gazdei curente este menținut pentru a realiza această distribuție.
    - Shortest Queue: Sarcinile sunt asignate gazdei cu coada de sarcini cea mai scurtă, determinată de numărul de sarcini deja în coadă.
    - Asignare Sarcini cu Interval de Dimensiune: Sarcinile sunt asignate gazdelor în funcție de tipul lor, categorisat ca "SHORT," "MEDIUM" sau "LONG." Se menține o mapare de la tipul de sarcină la indexul gazdei.
    - Cea mai Mică Lucrare Rămasă: Sarcinile sunt asignate gazdei cu cea mai mică cantitate de muncă rămasă pentru a-și finaliza sarcinile.
    
    Siguranță în Firul de Execuție
    ---------------------------------
    Pentru a asigura siguranța în firul de execuție, se folosește un ReentrantLock (mutex). Acest lucru este dobândit înainte de executarea secțiunilor critice ale codului și este eliberat ulterior pentru a preveni modificările concurente.
    
    Detalii de Implementare
    -------------------------
    - Asignare Sarcini Round Robin: Gazdele sunt accesate într-un mod round-robin, asigurând o distribuție uniformă a sarcinilor în rândul gazdelor disponibile.
    - Asignare Sarcini cu Coada cea mai Scurtă: Gazda cu coada cea mai scurtă este determinată folosind o operație de stream pe lista de gazde și este asignată sarcina primită.
    - Asignare Sarcini cu Interval de Dimensiune: Tipul sarcinii primite este utilizat pentru a identifica indexul corespunzător al gazdei din maparea predefinită, iar sarcina este asignată în consecință.
    - Asignare Sarcini cu Cea mai Mică Lucrare Rămasă: Gazda cu cea mai mică cantitate de muncă rămasă este identificată folosind o operație de stream și i se asignă sarcina primită.
    
    Concluzie
    -------------------------
    Clasa MyDispatcher oferă o metodă flexibilă și eficientă pentru asignarea sarcinilor către nodurile de calcul, adaptându-se la diverse algoritme de programare, menținând în același timp siguranța firului de execuție prin utilizarea blocărilor.


Implementare MyHost
---------------------------------------------

    Implementarea clasei MyHost
    -----------------------------
    
    Clasa MyHost extinde clasa Host și implementează logica de planificare a sarcinilor pe un nod de calcul.
    
    Atribute
    -----------------------------
    - waitList: O coadă de priorități care stochează sarcinile așteptând să fie procesate. Sarcinile sunt ordonate în funcție de prioritatea lor, cu cele mai prioritare sarcini având prioritatea cea mai mare.
    - inRun: Reprezintă sarcina care este în execuție pe nodul curent de calcul.
    - run: Un flag care indică dacă nodul de calcul este activ și în funcțiune.
    - lock: Un obiect de tip ReentrantLock(mutex) utilizat pentru a asigura accesul sincronizat la secțiuni critice ale codului pentru a evita condițiile de cursă.
    
    Metode
    -----------------------------
    - run(): Metodă suprascrisă din clasa Thread care conține bucla principală de execuție a nodului de calcul. Verifică dacă există sarcini în așteptare și le procesează în mod corespunzător.
    - processTasks(): Metodă care implementează logica de procesare a sarcinilor. Verifică dacă există sarcini în așteptare sau dacă există deja o sarcină în execuție. Dacă există, încearcă să preempționeze sarcina dacă este posibil.
    - handleInterruptedException(): Metodă care gestionează excepția InterruptedException în mod corespunzător. În acest caz, se așteaptă 500 de milisecunde pentru a simula un interval de timp în care sarcina este în execuție.
    - preemptTask(): Metodă care preempeționează sarcina curentă dacă există o sarcină mai prioritară în coada de așteptare.
    - addTask(Task task): Metodă pentru adăugarea unei noi sarcini în coada de așteptare.
    - getQueueSize(): Metodă care returnează dimensiunea cozii de așteptare, inclusiv sarcina aflată în execuție.
    - getWorkLeft(): Metodă care calculează cantitatea totală de muncă rămasă pe nodul de calcul, luând în considerare sarcinile aflate în așteptare și sarcina în execuție.
    - shutdown(): Metodă care oprește nodul de calcul prin setarea flag-ului run la false.
    
    Concluzie 
    -----------------------------
    Această implementare asigură sincronizare corectă în ceea ce privește accesul concurent la datele critice și oferă o soluție eficientă pentru planificarea sarcinilor pe un nod de calcul.

