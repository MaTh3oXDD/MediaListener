package org.example.Hibernate;

import jakarta.persistence.*;

@Entity
@Table(name = "music")
public class music {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "song_name")
    private String song_name;
    @Column(name = "views")
    private int views;
    @Column(name = "likes")
    private int likes;
    @Lob
    @Column(name = "cover_image")
    private byte[] coverImage;
    public music() {
    }

    public music(int id, String song_name, int views, int likes) {
        this.id = id;
        this.song_name = song_name;
        this.views = views;
        this.likes = likes;
    }

    public music(int id, String song_name, int views, int likes, byte[] coverImage) {
        this.id = id;
        this.song_name = song_name;
        this.views = views;
        this.likes = likes;
        this.coverImage = coverImage;
    }

    public byte[] getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(byte[] coverImage) {
        this.coverImage = coverImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSong_name() {
        return song_name;
    }

    public void setSong_name(String song_name) {
        this.song_name = song_name;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
}
