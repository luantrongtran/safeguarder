package ifn701.safeguarder.Parcelable;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

import ifn701.safeguarder.backend.myApi.model.Accident;
import ifn701.safeguarder.backend.myApi.model.AccidentList;

/**
 * Created by lua on 4/09/2015.
 */
public class AccidentListParcelable implements Parcelable {
    public AccidentList getAccidentList() {
        return accidentList;
    }

    public void setAccidentList(AccidentList accidentList) {
        this.accidentList = accidentList;
    }

    AccidentList accidentList;
    AccidentParcelable[] accidentParcelableList;

    public AccidentListParcelable(AccidentList accidentList) {
        this.accidentList = accidentList;

        accidentParcelableList = new AccidentParcelable[accidentList.getAccidentList().size()];
        int k = 0;
        for(Accident acc : accidentList.getAccidentList()) {
            accidentParcelableList[k++] = new AccidentParcelable(acc);
        }
    }

    public AccidentListParcelable(Parcel in) {
        List<AccidentParcelable> lstParcelable = null;
        in.readTypedList(lstParcelable, CREATOR);
        lstParcelable.toArray(accidentParcelableList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedArray(accidentParcelableList, flags);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public AccidentListParcelable createFromParcel(Parcel in) {
            AccidentListParcelable parc = new AccidentListParcelable(in);
            return parc;
        }

        public AccidentListParcelable[] newArray(int size) {
            return new AccidentListParcelable[size];
        }
    };
}
