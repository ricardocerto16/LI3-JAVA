package engine;

import java.util.Objects;

public class Revision {

     private String title;
     private long artId;
     private long revId;
     private long contId;
     private String timestamp;
     private String username;
     private long words;
     private long bytes;
     private int number;

     public Revision(){
          this.artId = 0;
          this.revId = 0;
          this.contId = 0;
          this.title = "";
          this.timestamp = "";
          this.username = "";
          this.words = 0;
          this.bytes = 0;
          this.number = 1;
     }

     public Revision(String title, long artId, long revId, long contId, String timestamp, String username, long words, long bytes, int number){
          this.artId = artId;
          this.revId = revId;
          this.contId = contId;
          this.title = title;
          this.timestamp = timestamp;
          this.username = username;
          this.words = words;
          this.bytes = bytes;
          this.number = number;
     }


    public long getArtId() {
        return artId;
     }
     
    public void setArtId(Long id) {
        this.artId = id;
    }
    public long getRevId() {
        return revId;
     }
    public void setRevId(Long id) {
        this.revId = id;
    }
    public long getContId() {
        return contId;
     }
    public void setContId(Long id) {
        this.contId = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String name) {
        this.username = name;
    }
    public String getTime() {
        return timestamp;
    }
    public void setTime(String time) {
        this.timestamp = time;
    }
    public long getWords() {
        return words;
     }
    public void setWords(Long words) {
        this.words = words;
    }
    public long getBytes() {
        return bytes;
     }
    public void setBytes(Long bytes) {
        this.bytes = bytes;
    }
    public int getNumber() {
        return this.number;
    }
    public void addNumber(){
        this.number += 1;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Revision other = (Revision) obj;
        if (this.revId != other.revId) {
            return false;
        }
        return true;
    }

     @Override
     public String toString() {
          return "<" + artId + ", " + title + ", " + revId+ ", " + timestamp+ ", " + username+ ", " + contId+ ", " + words + ", "+ bytes + ">";
     }
}