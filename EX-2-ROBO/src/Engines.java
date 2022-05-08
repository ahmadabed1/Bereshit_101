
public class Engines {

	private boolean Landing;
	public double currentRotation;
	double ang=58.3;
	Main_Engine FrontRight1,FrontRight2,FrontLeft1,FrontLeft2,BackRight1,BackRight2,BackLeft1,BackLeft2;
	Bereshit_101 Bereshit_A;

	public Engines(Bereshit_101 b) {
		init();
		Bereshit_A = b;

	}
	public void init() { //8 engines
		FrontRight1 = new Main_Engine(this, 1);
		FrontRight2 = new Main_Engine(this,0.75);
		FrontLeft1 = new Main_Engine(this,1);
		FrontLeft2 = new Main_Engine(this,0.75);
		BackRight1 = new Main_Engine(this,-1);
		BackRight2 = new Main_Engine(this,-0.75);
		BackLeft1 = new Main_Engine(this,-1);
		BackLeft2 = new Main_Engine(this,-0.75);
		Landing=false;
	}

	public void startLanding() {//defult
		Landing=true;
	}

	public void balance() { //to balance the engines in brishit
		currentRotation = Bereshit_A.ang;
		if(!Landing) {
			if(currentRotation > ang+4) {
				BackRight1.power();
				BackLeft1.power();
			}
			else if(currentRotation > ang+3) {
				BackRight1.power();
				BackLeft2.power();
			}
			else if(currentRotation > ang+1) {
				BackRight2.power();
			}
			else if(currentRotation < ang+5) {
				FrontRight1.power();
				FrontLeft1.power();
			}
			else if(currentRotation < ang+3) {
				FrontRight1.power();
				FrontLeft2.power();
			}
			else if(currentRotation < ang+1) {
				FrontRight2.power();
			}
		}
		else {
			BackRight1.power();
			FrontRight1.power();
			BackLeft2.power();
		}

	}
	public class Main_Engine {
		double Rotationpower;
		Engines MainEngines;

		public Main_Engine(Engines engines, double powerUp) {
			this.Rotationpower = powerUp;
			MainEngines = engines;
		}

		public void power() {
			if(MainEngines.Bereshit_A.ang > 0) {
				MainEngines.Bereshit_A.ang += Rotationpower;
			}
		}
	}



}
