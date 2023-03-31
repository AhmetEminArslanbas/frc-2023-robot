// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;



import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
public class Robot extends TimedRobot {

  private static PWMSparkMax left_m = new PWMSparkMax(0);
  private static PWMSparkMax right_m  = new PWMSparkMax(1);
  private static PWMSparkMax kol  = new PWMSparkMax(2);
  private static PWMSparkMax el  = new PWMSparkMax(3);
  private static PWMSparkMax kolgeri  = new PWMSparkMax(4);
  private static Joystick joystick = new Joystick(0);
  private static DifferentialDrive robot_drive = new DifferentialDrive(left_m, right_m);
  double arcade_x = 0;
  double arcade_y = 0;
  double kol_ileri = 0;
  double kol_geri = 0;
  double el_ileri = 0; 
  double el_geri = 0;

  float time = 0;
 
  double hizlan = 0;
  double yavasla = 0;
  boolean axis_4 = false;
  boolean axis_5 = false;

  double ileri = 0.60;//0.80
  double cevir = 0.55;//0.50

  double kol_hizi = 0;
  double kol_ayar_hizi = 1;

  double n = 0;
  double startTie = 0;

  boolean kol_hareket_etti_mi = false;
  boolean buton_basildi_mi = false;
  boolean kol_hareket = false;
  boolean koll_geri = false;
  
  




  @Override
  public void robotInit() {

  }

  @Override
  public void robotPeriodic() {}

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() 
  {
    n = 0;
    if(n == 0)
    {
      startTie = System.currentTimeMillis();
      n = 1;
    }
    
    
        
      
    
    
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() 
  {
    double gecen_sure = 0;// bu değeri n == 0 yerine de 0 diye eşitle !!!


    if(System.currentTimeMillis() - startTie <= (gecen_sure = gecen_sure + 800))// 0.5 saniye kolu geri çek
    {
      kolgeri.setVoltage(4);
    }
    else if(System.currentTimeMillis() - startTie <= (gecen_sure = gecen_sure + 600))// 0.5 saniye kolu yarım yukarı kaldır
    {
      kol.setVoltage(5);
    }
    else if(System.currentTimeMillis() - startTie <= (gecen_sure = gecen_sure + 500))// 0.5 saniye kolu geri indir
    {
      kol.setVoltage(4);
    }
    else if(System.currentTimeMillis() - startTie <= (gecen_sure = gecen_sure + 600))// 0.5 saniye kolu geri indir
    {
      kolgeri.setVoltage(-3.5);
    }
    else if(System.currentTimeMillis() - startTie <= (gecen_sure = gecen_sure + 1500))// 1.5 saniye kolu tam kaldır
    {
      kolgeri.setVoltage(0);
      kol.setVoltage(6);
    }
    else if(System.currentTimeMillis() - startTie <= (gecen_sure = gecen_sure + 5000))// 2 saniye ileri git
    {
      driv(0.5,0);
    }
    else if(System.currentTimeMillis() - startTie <= (gecen_sure = gecen_sure + 500))// 0.5 saniye kolu yavaşça aşa indir
    {
      kol.setVoltage(0.75);
    }
    else if(System.currentTimeMillis() - startTie <= (gecen_sure = gecen_sure + 500))// 0.5 saniye eli aç
    {
    }
    else if(System.currentTimeMillis() - startTie <= (gecen_sure = gecen_sure + 400))// 0.5 saniye eli aç
    {
      el.setVoltage(8);
    }
    else if(System.currentTimeMillis() - startTie <= (gecen_sure = gecen_sure + 500))// 0.5 saniye kolu yukarı kaldır.
    {
      kol.setVoltage(4.5);
      el.setVoltage(0);
    }
    else if(System.currentTimeMillis() - startTie <= (gecen_sure = gecen_sure + 3000))// 4.5 saniye geri git (Bu değer 4.5 metre geri gitme metresine göre değişecek)
    {
      driv(-0.75,0);
    }
    else// kolu yavaşça aşağı indir
    {
      kol.setVoltage(0.65);
    }

    
  }



  public void driv(double s1, double r1)
  {
    robot_drive.arcadeDrive(r1, -s1);
  }



  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() 
  {
      
    
  
  }


  




  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() 
  {
      //yavasla = joystick.getButton(0)
      arcade_y = joystick.getRawAxis(4); //sağ sol
      arcade_x = joystick.getY(); //İleri
      kol_ileri = joystick.getRawAxis(3);
      kol_geri = joystick.getRawAxis(2);
      //el_ileri = joystick.getRawButton(2);

      System.out.print(arcade_x);
      arcade_y = arcade_y * cevir;
      arcade_x = arcade_x * ileri;


      //







      /*
      if(joystick.getRawButtonPressed(1) == true)//    bunu kapat
      {
        if(buton_basildi_mi == false)
        {
          kol.setVoltage(-4.5);
          buton_basildi_mi = true;
        }
        else
        {
          kol.setVoltage(0);
          buton_basildi_mi = false;
        }
      }*/






      if(joystick.getRawButton(6) == true)
      {
        ileri = 1.5;//1.5
        cevir = 0.50;


        
        kol_ayar_hizi = 2.0;
      }
      else if(joystick.getRawButton(5) == true)
      {
        ileri = 0.425;
        cevir = 0.50;
        kol_ayar_hizi = 0.75;
      }else
      {
        ileri = 0.7;
        cevir = 0.55;
        kol_ayar_hizi = 1;
      }


      
      

      //
      if(kol_ileri > 0.1)
      {
        //kol.setVoltage(-kol_geri * 5 * hareket);//5
        kol_hizi = 2.0;
        el.setVoltage(0);
        kol_hareket = true;
        kol_hareket_etti_mi = true;
        koll_geri = false;
      }
      else if(kol_geri > 0.1)//<
      {
        //kol.setVoltage(kol_ileri * 2 * hareket);//2  
        kol_hizi = -2.5;
        el.setVoltage(0);
      }
      else 
      {
        kol_hizi = 0;
      }
      System.out.print(kol_hareket_etti_mi);
      System.out.println(koll_geri);

      if(kol_hareket_etti_mi == true)   //bunu aç
      {
        if(koll_geri == true)
        {
          kol.setVoltage(0);
          kolgeri.setVoltage(4.5);
        }
        else
        {
          kol.setVoltage(3.25 + kol_hizi * kol_ayar_hizi);//kol_hizi 2.0
        }
        
        if(joystick.getRawButton(2) == true)
        {
          el.setVoltage(8);
        }
        else if(joystick.getRawButton(3) == true)
        {
          el.setVoltage(-10);//5.5
        }
        else
        {
          el.setVoltage(0);
        }

        if(joystick.getRawButtonPressed(4) == true)
        {
          kol_hareket_etti_mi = false;
          kol.setVoltage(0);
        }

        
        if(joystick.getRawButtonPressed(1) == true)
        {
          koll_geri = true;
        }


        if(joystick.getPOV() == 0)
        {
          kolgeri.setVoltage(5);
        }
        else if(joystick.getPOV() == 180)
        {
          kolgeri.setVoltage(-1.75);
        }
        else if(koll_geri == false) 
        {
          kolgeri.setVoltage(0);
        }
      }
      else 
      {
        kol.setVoltage(0);
        kolgeri.setVoltage(0);
        el.setVoltage(0);
      }

      
    
      robot_drive.arcadeDrive(arcade_y, arcade_x);
      
  }
    

  public void kol_kaldir()
  {
    double startTie_1 = System.currentTimeMillis();
    double gecen_sure_1 = 0;
    System.out.println(startTie_1);

    if(System.currentTimeMillis() - startTie_1 <= startTie_1 + (gecen_sure_1 = gecen_sure_1 + 500))// 0.5 saniye kolu geri çek
    {
      kolgeri.setVoltage(4);
    }
    else if(System.currentTimeMillis() - startTie_1 <= startTie_1 + (gecen_sure_1 = gecen_sure_1 + 400))// 0.5 saniye kolu yarım yukarı kaldır
    {
      kol.setVoltage(4);
    }
    else if(System.currentTimeMillis() - startTie_1 <= startTie_1 + (gecen_sure_1 = gecen_sure_1 + 800))// 0.5 saniye kolu geri indir
    {
      kolgeri.setVoltage(-3.5);
    }
    else if(System.currentTimeMillis() - startTie_1 <= startTie_1 + (gecen_sure_1 = gecen_sure_1 + 100))// 0.5 saniye kolu geri indir
    {
      kol_hareket_etti_mi = false;
    }
  }

  public void kolu_indir()
  {
    double startTie_2 = System.currentTimeMillis();
    double gecen_sure_2 = 0;
    System.out.println(startTie_2);

    if(System.currentTimeMillis() - startTie_2 <= startTie_2 + (gecen_sure_2 = gecen_sure_2 + 250))// 0.5 saniye kolu geri çek
    {
      kol.setVoltage(3.5);
    }
    else if(System.currentTimeMillis() - startTie_2 <= startTie_2 + (gecen_sure_2 = gecen_sure_2 + 350))// 0.5 saniye kolu yarım yukarı kaldır
    {
      kolgeri.setVoltage(5);
    }
    else if(System.currentTimeMillis() - startTie_2 <= startTie_2 + (gecen_sure_2 = gecen_sure_2 + 400))// 0.5 saniye kolu geri indir
    {
      kol.setVoltage(0.5);
    }
    else if(System.currentTimeMillis() - startTie_2 <= startTie_2 + (gecen_sure_2 = gecen_sure_2 + 400))// 0.5 saniye kolu geri indir
    {
      kol_hareket_etti_mi = false;
    }
  } 
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  // Simulation only: physics models and field/mechanism views live in RobotSim.
  private RobotSim sim;

  @Override
  public void simulationInit() {
    sim = new RobotSim(left_m, right_m, kol, kolgeri, el);
  }

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {
    sim.update();
  }
}