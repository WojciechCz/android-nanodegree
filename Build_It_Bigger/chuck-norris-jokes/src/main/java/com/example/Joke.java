package com.example;

/**
 * Created by fares on 02.01.16.
 */
public class Joke {

    private final String[] mJokes = new String[] {
            "Chuck Norris uses ribbed condoms inside out, so he gets the pleasure.",
            "There are no steroids in baseball. Just players Chuck Norris has breathed on.",
            "Chuck Norris doesn't read books. He stares them down until he gets the information he wants.",
            "In an average living room there are 1,242 objects Chuck Norris could use to kill you, including the room itself.",
            "CNN was originally created as the \"Chuck Norris Network\" to update Americans with on-the-spot ass kicking in real-time."
    };

    private int mCounter = 0;

    public String[] getAll(){
        return mJokes;
    }

    public String getNext(){
        return mJokes[mCounter++%mJokes.length];
    }

}
