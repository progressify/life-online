package it.bdsir.life_online;

class Singleton {
	private static final Object mLock = new Object();
	static Singleton mInstance;
	private String id;
	private String ente,latitudine,longitudine,numero;
	private boolean gpsEnabled;

	private Singleton() {
		id="";
		gpsEnabled=true;
		ente="";
		latitudine="-1";
		longitudine="-1";
		numero="";
	}

	static Singleton getInstance() {
		synchronized (mLock) {
			if (mInstance == null) {
				mInstance = new Singleton();
			}
			return mInstance;
		}
	}

	public void del(){
		gpsEnabled=true;
		id="";
		ente="";
		latitudine="-1";
		longitudine="-1";
		numero="";
	}

	public String getLatitudine() {
		return latitudine;
	}

	public void setLatitudine(String latitudine) {
		this.latitudine = latitudine;
	}

	public String getLongitudine() {
		return longitudine;
	}

	public void setLongitudine(String longitudine) {
		this.longitudine = longitudine;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getEnte() {
		return ente;
	}
	public void setEnte(String ente) {
		this.ente = ente;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isGpsEnabled() {
		return gpsEnabled;
	}

	public void setGpsEnabled(boolean gpsEnabled) {
		this.gpsEnabled = gpsEnabled;
	}
}