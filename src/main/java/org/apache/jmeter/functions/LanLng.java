package org.apache.jmeter.functions;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import net.sf.json.JSONObject;
import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.functions.AbstractFunction;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class LanLng extends AbstractFunction {
	private static final List<String> desc = new LinkedList<String>();
	private static final String KEY = "__lan_lng_distance";
	private static final int MAX_PARAM_COUNT = 4;
	private static final int MIN_PARAM_COUNT = 4;
	private static final Logger log = LoggingManager.getLoggerForClass();
	
	private Object[] values;
	/**
	 * 求B点经纬度
	 * @param A 已知点的经纬度，
	 * @param distance   AB两地的距离  单位km
	 * @param angle  AB连线与正北方向的夹角（0~360）
	 * @return  B点的经纬度
	 */
	final static double Rc=6378137;
	final static double Rj=6356725;
	double m_LoDeg,m_LoMin,m_LoSec;
	double m_LaDeg,m_LaMin,m_LaSec;
	double m_Longitude,m_Latitude;
	double m_RadLo,m_RadLa;
	double Ec;
	double Ed;
	public LanLng(double latitude,double longitude){
		m_LoDeg=(int)longitude;
		m_LoMin=(int)((longitude-m_LoDeg)*60);
		m_LoSec=(longitude-m_LoDeg-m_LoMin/60.)*3600;

		m_LaDeg=(int)latitude;
		m_LaMin=(int)((latitude-m_LaDeg)*60);
		m_LaSec=(latitude-m_LaDeg-m_LaMin/60.)*3600;

		m_Longitude=longitude;
		m_Latitude=latitude;
		m_RadLo=longitude*Math.PI/180.;
		m_RadLa=latitude*Math.PI/180.;
		Ec=Rj+(Rc-Rj)*(90.-m_Latitude)/90.;
		Ed=Ec*Math.cos(m_RadLa);
	}
	public static String getMyLatLng(LanLng A,double distance,double angle){//方法
		double dx = distance*1000*Math.sin(Math.toRadians(angle));
		double dy= distance*1000*Math.cos(Math.toRadians(angle));
		double bjd=(dx/A.Ed+A.m_RadLo)*180./Math.PI;
		double bwd=(dy/A.Ec+A.m_RadLa)*180./Math.PI;
		String lnglat="{lat="+bwd+","+"lng="+bjd+"}";
		return lnglat;
	}

	static {
		desc.add("latitude");
		desc.add("longitude");
		desc.add("distance(km)");
		desc.add("angle(0-360)");
	}
	/**
	 * No-arg constructor.
	 */
	public LanLng() {
		super();
	}

	/** {@inheritDoc} */
	@Override
	public synchronized String execute(SampleResult previousResult, Sampler currentSampler)
			throws InvalidVariableException {
		
		String lat = new String(((CompoundVariable) values[0]).execute().trim());
		String lng = new String(((CompoundVariable) values[1]).execute().trim());
		double distance = new Double(((CompoundVariable) values[2]).execute().trim());
		double angle = new Double(((CompoundVariable) values[3]).execute().trim());
		//LanLng A = new LanLng(104.08111, 30.5457);
		LanLng A = new LanLng(Double.parseDouble(lat), Double.parseDouble(lng));
		String latlng = getMyLatLng(A, distance, angle);
		JSONObject object = JSONObject.fromObject(latlng);
		String latLngJson=object.toString();
		log.info(latLngJson);
		return latLngJson;
	}

	/** {@inheritDoc} */
	@Override
	public String getReferenceKey() {
		return KEY;
	}

	@Override
	public List<String> getArgumentDesc() {
		return desc;
	}

	@Override
	public void setParameters(Collection<CompoundVariable> parameters)
			throws InvalidVariableException {
		// TODO Auto-generated method stub
		checkParameterCount(parameters, MIN_PARAM_COUNT, MAX_PARAM_COUNT);
		values = parameters.toArray();
	}
}
