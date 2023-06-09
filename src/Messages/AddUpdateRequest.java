/*
    @Author: Yinghua Zhou
    Student ID: 1308266
 */

package Messages;

import Utils.Operation;

import java.util.ArrayList;

public class AddUpdateRequest extends Request{
    private String word;
    private ArrayList<String> meanings;

    public AddUpdateRequest(Operation op)
    {
        super(op);
        this.word = null;
        this.meanings = new ArrayList<>();
    }

    public AddUpdateRequest(Operation op, String word, ArrayList<String> meanings) {
        super(op, word);
        this.word = word;
        this.meanings = meanings;
    }

    public String getWord() {
        return word;
    }

    public ArrayList<String> getMeanings() {
        return meanings;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setMeanings(ArrayList<String> meanings) {
        this.meanings = meanings;
    }

    public void addMeaning(String meaning) {
        this.meanings.add(meaning);
    }

    @Override
    public String toString() {
        return "Request{" +
                "operation='" + super.getOp() + '\'' +
                ", word='" + word + '\'' +
                ", meanings=" + meanings +
                '}';
    }
}
