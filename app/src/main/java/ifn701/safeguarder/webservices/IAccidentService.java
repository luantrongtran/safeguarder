package ifn701.safeguarder.webservices;


import ifn701.safeguarder.backend.myApi.model.Accident;

public interface IAccidentService {
    public void onNewAccidentReported(Accident accident);
}
