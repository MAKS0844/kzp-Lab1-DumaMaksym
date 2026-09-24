package ua.lpnu.kzp;

public class Movie {
    public final String title;
    public final String director;
    public final int minutes;
    public final int year;
    public final double rating;

    public Movie(String title, String director, int minutes, int year, double rating) {
        this.title = title;
        this.director = director;
        this.minutes = minutes;
        this.year = year;
        this.rating = rating;
    }
}
