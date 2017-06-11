package engine;

import li3.Interface;
import engine.Revision;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

public class QueryEngineImpl implements Interface {
    private TreeMap<Long, List<Revision>> wiki;
    private long allArt;
    private long allRev;
    private String name;
    private String title;
    private String time;
    private Map<Long,Integer> t10;
    private Map<Long,Long> t20;
    private Map<Long,Long> tN;

    /* função de inicialização das estruturas */
    public void init() {
        wiki = new TreeMap<Long, List<Revision>>();
        t10 = new HashMap<>();
        t20 = new HashMap<>();
        tN = new HashMap<>();
        allArt = 0;
        allRev = 0;
        name = "";
        title = "";
        time = "";
    }

    /* load das infos para a estrutura */
    public void load(int nsnaps, ArrayList<String> snaps_paths){
        String[] args = new String[nsnaps];
        int i = 0;
        try{
            for(String arg: snaps_paths){
                args[i] = arg;
                i++;
            }
            Parser p = new Parser();
            wiki = p.parsing(args, nsnaps);
        }catch(Exception e){
            System.err.println(e);
        }
    }

    /* Querie 1 */
    public long all_articles() {
        wiki.values().stream()
                     .forEach(p -> {
                        for(Revision r: p){
                            allValue(r.getNumber(), 1);
                        };
                     });
        return this.allArt;
    }

    private void allValue(int size, int index){
        if(index == 1)
            this.allArt += size;
        else if(index == 2)
            this.allRev += size;
    }

    /* Querie 2 */ 
    public long unique_articles() {
        return wiki.size();        
    }

    /* Querie 3 */ 
    public long all_revisions() {
        wiki.values().stream()
                     .forEach(p -> {
                        allValue(p.size(), 2);
                     });

        return allRev;
    }

    /* Querie 4 */ 
    public ArrayList<Long> top_10_contributors() {
        ArrayList<Long> cont = new ArrayList<Long>();
        wiki.values().stream()
                     .forEach( p -> {
                        for(Revision rev : p) {
                            putValue(rev.getContId());
                        }
                     });

        cont = t10.entrySet().stream()
                             .sorted(Map.Entry.<Long, Integer>comparingByValue().reversed())
                             .limit(10)
                             .map(e -> e.getKey())
                             .collect(Collectors.toCollection(ArrayList::new));    

        return cont;
    }
    
    private void putValue(long id) {
        if (id != -1) {
            if (t10.containsKey(id)){
                t10.put(id, (t10.get(id)) + 1); 
            }
            else {
                t10.put(id,1);
            }
        }
    }

    /* Querie 5 */ 
    public String contributor_name(long contributor_id) {
        wiki.values().stream()
                     .forEach(p -> {
                        for(Revision r: p){
                            if(contributor_id == r.getContId()){
                                setString(r.getUsername(), 1);
                                break;
                            }
                        };
                     });
        return name;
    }

    private void setString(String s, int index){
        if(index == 1)
            name = s;
        else if(index == 2)
            title = s;
        else if(index == 3)
            time = s;
    }

    /* Querie 6 */ 
    public ArrayList<Long> top_20_largest_articles() {
        ArrayList<Long> cont = new ArrayList<Long>();
        wiki.values().stream()
                     .forEach( p -> {
                        for(Revision rev : p) {
                            putBytes(rev.getBytes(), rev.getArtId());
                        }
                     });

        cont = t20.entrySet().stream()
                             .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
                             .limit(20)
                             .map(e -> e.getKey())
                             .collect(Collectors.toCollection(ArrayList::new)); 

        return cont;
    }

    private void putBytes(long bytes, long idArt){
        if(t20.containsKey(idArt)){
            if(bytes > t20.get(idArt))
                t20.put(idArt, bytes);
        }
        else{
            t20.put(idArt, bytes);
        }
    }

    /* Querie 7 */ 
    public String article_title(long article_id) {
        wiki.values().stream()
                     .forEach(p -> {
                        for(Revision r: p){
                            if(article_id == r.getArtId()){
                                setString(r.getTitle(), 2);
                                break;
                            }
                        };
                     });

        return title;
    }

    /* Querie 8 */ 
    public ArrayList<Long> top_N_articles_with_more_words(int n) {
        ArrayList<Long> cont = new ArrayList<Long>();
        wiki.values().stream()
                     .forEach( p -> {
                        for(Revision rev : p) {
                            putWords(rev.getWords(), rev.getArtId());
                        }
                     });

        cont = tN.entrySet().stream()
                             .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
                             .limit(n)
                             .map(e -> e.getKey())
                             .collect(Collectors.toCollection(ArrayList::new));

        return cont;
    }

    private void putWords(long words, long idArt){
        if(tN.containsKey(idArt)){
            if(words > tN.get(idArt))
                tN.put(idArt, words);
        }
        else{
            tN.put(idArt, words);
        }
    }

    /* Querie 9 */ 
    public ArrayList<String> titles_with_prefix(String prefix) {
        ArrayList<String> list = new ArrayList<String>();
        wiki.values().stream()
                    .forEach(p -> {
                list.addAll(p.stream().filter(r->r.getTitle().startsWith(prefix))
                    .map(r->r.getTitle())
                    .sorted()
                    .distinct()
                    .collect(Collectors.toCollection(ArrayList::new)));
                });
        
                    Collections.sort(list);
                    return list;
    }
    /* Querie 10 */ 
    public String article_timestamp(long article_id, long revision_id) {

        wiki.values().stream()
                     .forEach(p -> {
                        for(Revision r: p){
                            if(article_id == r.getArtId()){
                                if(revision_id == r.getRevId()){
                                setString(r.getTime(), 3);
                                break;}
                            }
                        };
                     });

        return time;
    }

    /* função de clean */
    public void clean() {
        wiki.clear();
        t10.clear();
        t20.clear();
        tN.clear();
    }
}
