package pso;

import java.util.Arrays;import java.util.concurrent.atomic.AtomicInteger;


public class Particle {

	private Position position;
	private Position lastPosition1;
	private Position lastPosition2;
	private Position pBest;
	private Velocity velocity;
	private double pBestVal = 999999.9f;
	private IFitness fitnessFunction;
	private PsoConfigOptions options;
	private int numDimensions;
	
	public Particle (Position position, Velocity velocity, IFitness fitnessFunction, PsoConfigOptions options) {
		this.position = position;
		this.velocity = velocity;
		this.numDimensions = position.getNumDimensions();
		this.fitnessFunction = fitnessFunction;
		this.options = options;
		this.pBest = position.copy();
		this.lastPosition1 = position.copy();
		this.lastPosition2 = position.copy();
	}
	
	public Particle (Position position, Velocity velocity) {
		this.position = position;
		this.velocity = velocity;
		this.numDimensions = position.getNumDimensions();
	}
	
	public float evaluateFitness () {
		float fitness = fitnessFunction.calcFitness(this);
		if (fitness < this.pBestVal) {
			this.pBestVal = fitness;
			this.pBest = this.position.copy();
		}
		return fitness;
	}
	
	public void update (Position gBest, double[] dimensionWeight) {
		if (gBest == null) {
			System.out.println("gBest is null, exiting now");
			System.exit(0);
		}
		//v[] = v[] + c1 * rand() * (pbest[] - present[]) + c2 * rand() * (gbest[] - present[])
		//double[] vel = new double[this.numDimensions];
		AtomicInteger indexCnt = new AtomicInteger(0);
		double[] updatedVelocity = Arrays.stream( velocity.getVector() )
				.map(scalarElement -> {
					int index = indexCnt.getAndIncrement();
					double personalBest = options.c1 * 1.0 * (pBest.getElement(index) - position.getElement(index));
					double globalBest = options.c2 * 1.0 * (gBest.getElement(index) - position.getElement(index));
					return (scalarElement + personalBest + globalBest) * dimensionWeight[index];
				})
				.toArray();
		this.velocity.setVector(updatedVelocity);
		
		this.validateSpeed();
		this.updateVector();
	}
	
	private void validateSpeed () {
		double elementsSquared = Arrays.stream( velocity.getVector() )
				.map(scalarElement -> Math.pow(scalarElement, 2))
				.sum();
		double vectorLength = Math.sqrt(elementsSquared);
		
		if (vectorLength > options.speedLimit) {
			this.applySpeedLimit(vectorLength);
		}
	}
	
	private void applySpeedLimit (double vectorLength) {
		//given a vector v with desired length L
		//u = (L / ||v||) v
		
		double desiredSpeedRatio = this.options.speedLimit / vectorLength;
		double[] appliedSpeedLimit = Arrays.stream( this.velocity.getVector() )
				.map(scalarElement -> desiredSpeedRatio * scalarElement)
				.toArray();
		this.velocity.setVector(appliedSpeedLimit);
	}
	
	private void updateVector () {
		this.lastPosition2 = this.lastPosition1.copy();
		this.lastPosition1 = this.position.copy();
		
		int[] newPos = new int[this.numDimensions];
		for (int i = 0; i < this.numDimensions; i++) {
			newPos[i] = (int) Math.round(this.position.getElement(i) + this.velocity.getElement(i));
		}
		this.position.setVector(newPos);
	}
	
	public double getLocalBest () {
		return this.pBestVal;
	}
	
	public Position getLocalBestPosition () {
		return this.pBest;
	}

	public Position getPosition () {
		return this.position;
	}
	
	public Position getLastPosition1 () {
		return this.lastPosition1;
	}
	
	public Position getLastPosition2 () {
		return this.lastPosition2;
	}
	
	
	
	public void reset () {
		this.pBestVal = 999999.9f;
	}
	
	public void scatter (Velocity velocity) {
		this.pBestVal = 999999.9f;
		this.velocity = velocity;
	}
	
	public void setPosition (Position position) {
		this.position = position;
		this.lastPosition1 = position.copy();
		this.lastPosition2 = position.copy();
	}
	
	public void setVelocity (Velocity velocity) {
		this.velocity = velocity;
	}
	
	public Velocity getVelocity () {
		return this.velocity;
	}
	
}
