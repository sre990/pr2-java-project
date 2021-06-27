## Programmazione 2 - AA 2020-2021
### Primo progetto intermedio

Il progetto ha l’obiettivo di applicare i concetti e le tecniche di programmazione *Object-Oriented* esaminate durante il corso. Lo scopo del progetto è lo sviluppo di una componente software di supporto alla gestione e l’analisi di una rete sociale (Social Network) denominata MicroBlog.

La rete sociale consente agli utenti di inviare messaggi di testo di breve lunghezza, con un massimo di 140 caratteri, chiamati post. Gli utenti possono 'seguire' i post di altri utenti.

**Parte 1**

Si richiede di progettare, realizzare e documentare il tipo di dato **Post** per rappresentare un post.
Un post è descritto da un insieme di informazioni:
* id: identificatore univoco del post
* author: utente della rete sociale che ha scritto il post
* text: testo (massimo 140 caratteri) del post
* timestamp: data e ora di invio del post

Si definisca la specifica completa del tipo di dato **Post**, introducendo i relativi metodi e fornendo le motivazioni delle scelte effettuate.
Possono essere aggiunte altre informazioni motivando opportunamente la scelta.

Si definisca l’implementazione del tipo di dato Post.

**Parte 2**

Si richiede di progettare, realizzare e documentare il tipo di dato SocialNetwork per operare sulla rete sociale MicroBlog. Come struttura di implementazione della rete sociale si richiede di utilizzare

**Map<String, Set<String>>**

Intuitivamente, *Map[a]* definisce l’insieme delle persone seguite nella rete sociale dall’utente *a*. Una persona sulla rete sociale è rappresentata e identificata in modo univoco dal nome. Gli utenti della rete sociale non possono seguire se stessi.

Si ipotizzi di avere a disposizione, tra gli altri, i seguenti metodi
1. **public Map<String, Set<String>> guessFollowers(List<Post> ps)** che restituisce la rete sociale derivata dalla lista di post (parametro del metodo);
2. **public List<String> influencers()** che restituisce gli utenti più influenti delle rete sociale, ovvero quelli che hanno un numero maggiore di "follower";
3. **public Set<String> getMentionedUsers()** che restituisce l’insieme degli utenti menzionati (inclusi) nei post presenti nella rete sociale
4. **public Set<String> getMentionedUsers(List<Post> ps)** che restituisce l’insieme degli utenti menzionati (inclusi) nella lista di post;
5. **public List<Post> writtenBy(String username)** che restituisce la lista dei post effettuati dall’utente nella rete sociale il cui nome è dato dal parametro username;
6. **public List<Post> writtenBy(List<Post> ps, String username)** che restituisce la lista dei post effettuati dall’utente il cui nome è dato dal parametro username presenti nella lista ps;
7. **public List<Post> containing(List<String> words)** che restituisce la lista dei post presenti nella rete sociale che includono almeno una delle parole presenti nella lista delle parole argomento del metodo.

Si definisca la specifica completa del tipo di dato SocialNetwork fornendo le motivazioni delle scelte effettuate. Devono essere aggiunti altri metodi motivando opportunamente la scelta.

Si definisca l’implementazione del tipo di dato SocialNetwork.

**Parte 3**

Si richiede di discutete come sia possibile progettare una estensione gerarchica del tipo di dato SocialNetwork che permetta di introdurre un criterio per segnalare contenuti offensivi presenti nella rete sociale. Si presenti la progettazione e realizzazione di almeno una soluzione.

**Nota.**

Per valutare il comportamento di tutte le implementazioni proposte si realizzi una batteria di test.
Le implementazioni proposte devono superare tutte le batteria di test progettate.

