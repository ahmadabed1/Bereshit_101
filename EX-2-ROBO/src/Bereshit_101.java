import java.text.DecimalFormat;
public class Bereshit_101 {
	public static final double WEIGHT_EMP = 165; // kg
	public static final double WEIGHT_FULE = 420; // kg
	public static final double WEIGHT_FULL = WEIGHT_EMP + WEIGHT_FULE; // kg
	public static final double MAIN_ENG_F = 430; // N
	public static final double SECOND_ENG_F = 25; // N
	public static final double MAIN_BURN = 0.15; //liter per sec, 12 liter per m'
	public static final double SECOND_BURN = 0.009; //liter per sec 0.6 liter per m'
	public static final double ALL_BURN = MAIN_BURN + 8*SECOND_BURN;

	public static double Maxacc(double weight) {
		double t = 0;
		t += (8*SECOND_ENG_F) + MAIN_ENG_F;
		double ans = t/weight;
		return ans;
	}
	//the starting data
	Engines engines = new Engines(this);
	boolean landed = false;
	double Verticalspeed = 24.8;
	double HorizontalSpeed = 932; // HorizontalSpeed
	double dist = 181*1000; // Distance to land
	double ang = 58.3; // zero is vertical (as in landing) Orientation
	double alt = 13748;
	double time = 0; // Delta time
	double dt = 1; // sec
	double acc=0; // Acceleration rate (m/s^2)
	double distance=0;
	double fuel = 121;
	double weight = WEIGHT_EMP + fuel;
	double NN = 0.7; // rate[0,1]

	boolean DoThat() {
		if(alt < 2000) {
			engines.startLanding();
		}
		if(!landed) {
			engines.balance();
			Controle();
			Computing();
			Data();
			return true;
		}
		return false; //landed

	}

	private void Computing() {
		double ang_rad = Math.toRadians(ang);
		double h_acc = Math.sin(ang_rad)*acc;
		double v_acc = Math.cos(ang_rad)*acc;
		double vacc = Moon.getAcc(HorizontalSpeed);
		this.time+=dt;
		double AngularSpeed = (HorizontalSpeed/dist)/Math.toRadians(Math.PI*2);
		ang += AngularSpeed;
		if(NN>1) {NN=1;}
		double dWeight = dt*ALL_BURN*NN;// delta weight
		if(fuel>0) {
			fuel -= dWeight;
			weight = WEIGHT_EMP + fuel;
			acc = NN* Maxacc(weight);
		}
		else {
			acc=0;
		}
		v_acc -= vacc; // moon affects the vertical acceleration 
		if(HorizontalSpeed>0) {
			HorizontalSpeed -= h_acc*dt;}
		dist -= HorizontalSpeed*dt;
		Verticalspeed -= v_acc*dt;
		if(Verticalspeed < 0) { Verticalspeed = 1; }
		alt -= dt*Verticalspeed;
		distance += HorizontalSpeed*dt;
	}
	private void Controle() {
		if(alt <=1) {
			landed = true;
		}
		if(!landed) {
			// over 2000m above the ground
			if(alt>2000) {
				if(Verticalspeed >27) {
					NN+=0.003*dt;
				}
				if(Verticalspeed <18) {
					NN-=0.003*dt;
				} // maintain a vertical speed of [18-27] m/s
			}

			else {
				if(ang>3)
					engines.balance();
				if(alt>1000) {
					NN=0.58;
				}
				else if(alt>500) {
					NN=0.6;
				}
				else if(alt>100) {
					NN=0.70;
					if(Verticalspeed<20)NN=0.65;
				}
				else if(alt>40){
					NN=0.75;
					if(Verticalspeed<10 && Verticalspeed>5) {NN=0.7;}
				}
				else {
					if(Verticalspeed>5)NN=0.89;
					if(Verticalspeed<5 && Verticalspeed>3) {NN=0.75;}
					else if (Verticalspeed<3) {NN=0.9;}
				}
				if(HorizontalSpeed<2) {
					HorizontalSpeed=0;
				}
			}
		}
	}
	private void Data() {
		DecimalFormat form = new DecimalFormat("###.###");
		if(time % 10 == 0 || alt<100 )
			System.out.println("Time: "+form.format(time)+"  Height: "+form.format(alt)+" Verticalspeed: "
					+form.format(Verticalspeed)+" HorizontalSpeed: "+form.format(HorizontalSpeed)+ "  fuel:"+form.format(fuel)+"  Weight: "
					+form.format(weight) +"  Acceleration: " +form.format(acc) +"  Rotation: "
					+form.format(ang)+"  Distance: " +form.format((distance/1000))+" km"+" ");
		if(landed)
			System.out.println("Landed successfully");
	}

	public static void main(String[] args) {
		Bereshit_101 Bereshit_a= new Bereshit_101();
		boolean didnotLand=true;
		while(didnotLand) {
			didnotLand=Bereshit_a.DoThat();
		}
	}
}