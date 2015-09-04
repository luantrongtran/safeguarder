package ifn701.safeguarder.Parcelable;

import android.os.Parcel;
import android.os.Parcelable;

import ifn701.safeguarder.backend.myApi.model.Accident;

/**
 * Created by lua on 3/09/2015.
 */
public class AccidentParcelable implements Parcelable {

    private Accident accident;

    public AccidentParcelable(Accident acc) {
        this.accident = acc;
    }

    public AccidentParcelable(Parcel in) {
        accident = new Accident();
        accident.setId(in.readInt());
        accident.setUserId(in.readInt());
        accident.setName(in.readString());
        accident.setType(in.readString());
        accident.setTime(in.readLong());
        accident.setLat(in.readDouble());
        accident.setLon(in.readDouble());
        accident.setObservationLevel(in.readInt());
        accident.setDescription(in.readString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(accident.getId());
        dest.writeInt(accident.getUserId());
        dest.writeString(accident.getName());
        dest.writeString(accident.getType());
        dest.writeLong(accident.getTime());
        dest.writeDouble(accident.getLat());
        dest.writeDouble(accident.getLon());
        dest.writeInt(accident.getObservationLevel());
        dest.writeString(accident.getDescription());
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public AccidentParcelable createFromParcel(Parcel in) {
            AccidentParcelable lp = new AccidentParcelable(in);
            return lp;
        }

        public AccidentParcelable[] newArray(int size) {
            return new AccidentParcelable[size];
        }
    };
}
