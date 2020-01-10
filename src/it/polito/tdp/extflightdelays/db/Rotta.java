package it.polito.tdp.extflightdelays.db;

import it.polito.tdp.extflightdelays.model.Airport;

public class Rotta {
	private Airport source;
	private Airport target;
	private double avgDistance;
	
	public Rotta(Airport source, Airport target, double avgDistance) {
		super();
		this.source = source;
		this.target = target;
		this.avgDistance = avgDistance;
		
	}
	public Airport getSource() {
		return source;
	}
	public void setSource(Airport source) {
		this.source = source;
	}
	public Airport getTarget() {
		return target;
	}
	public void setTarget(Airport target) {
		this.target = target;
	}
	public double getAvgDistance() {
		return avgDistance;
	}
	public void setAvgDistance(double avgDistance) {
		this.avgDistance = avgDistance;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		result = prime * result + ((target == null) ? 0 : target.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rotta other = (Rotta) obj;
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		if (target == null) {
			if (other.target != null)
				return false;
		} else if (!target.equals(other.target))
			return false;
		return true;
	}
	
	

}
