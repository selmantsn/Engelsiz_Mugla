package cbs.engelsiz_mugla;

/**
 * Created by Asus on 18.03.2017.
 */
public class FotoEntity {
    private int id;
    private byte[] data;

    public FotoEntity(int id , byte[] data){
        this.id = id;
        this.data = data ;
    }

    public byte[] getData() {
        return data;
    }

    public int getId() {
        return id;
    }
}
