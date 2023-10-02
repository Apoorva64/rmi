package data;

import java.io.Serializable;

public sealed interface Pitch extends Serializable permits Pitch.TextPitch, Pitch.VideoPitch {
    void display();

    final class TextPitch implements Pitch {
        private final String text;

        public TextPitch(String text) {
            this.text = text;
        }

        @Override
        public void display() {
            System.out.println("This is my pitch: " + text);
        }
    }

    final class VideoPitch implements Pitch {
        private final String video;

        public VideoPitch(String video) {
            this.video = video;
        }

        // TODO: open link in browser
        //
        @Override
        public void display() {
            System.out.println("My video pitch is at: " + video);
        }

    }
}