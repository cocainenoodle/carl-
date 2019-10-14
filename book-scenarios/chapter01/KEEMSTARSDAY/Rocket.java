import greenfoot.*;  // (World, Actor, GreenfootImage, and Greenfoot)

/**
 * A rocket that can be controlled by the arrowkeys: up, left, right.
 * The gun is fired by hitting the 'space' key.
 * 
 * @author Poul Henriksen
 * @author Michael Kolling
 */
public class Rocket extends Mover
{
    private int gunReloadTime;                  // The minimum delay between firing the gun.
    private int reloadDelayCount;               // How long ago we fired the gun the last time.
    private Vector acceleration;                // How fast the rocket is.
    private int shotsFired;                     // Number of shots fired.
    
    private GreenfootImage rocket = new GreenfootImage("keemstar.png");    
    private GreenfootImage rocketWithThrust = new GreenfootImage("keemstar2.png");

    /**
     * Initilise this rocket.
     */
    public Rocket()
    {
        gunReloadTime = 1;
        reloadDelayCount = 0;
        acceleration = new Vector(0, 1.0);
        increaseSpeed(new Vector(13, 0.5)); // initially slowly drifting
        shotsFired = 0;
    }

    /**
     * Do what a rocket's gotta do. (Which is: mostly flying about, and turning,
     * accelerating and shooting when the right keys are pressed.)
     */
    public void act()
    {
        move();
        checkCollision();
        checkKeys();
        reloadDelayCount++;
    }
    
    /**
     * Return the number of shots fired from this rocket.
     */
    public int getShotsFired()
    {
        return shotsFired;
    }
    
    /**
     * Return the approximate current travelling speed of this rocket.
     */
    public int getSpeed()
    {
        return (int) (getMovement().getLength() * 10);
    }
    
    /**
     * Set the time needed for re-loading the rocket's gun. The shorter this time is,
     * the faster the rocket can fire. The (initial) standard time is 20.
     */
    public void setGunReloadTime(int reloadTime)
    {
        gunReloadTime = reloadTime;
    }
    
    /**
     * Check whether we are colliding with an asteroid.
     */
    private void checkCollision() 
    {
        Asteroid a = (Asteroid) getOneIntersectingObject(Asteroid.class);
        if(a != null) {
            getWorld().addObject(new Explosion(), getX(), getY());
            getWorld().removeObject(this);
        }
    }
    
    /**
     * Check whether there are any key pressed and react to them.
     */
    private void checkKeys() 
    {
        ignite(Greenfoot.isKeyDown("up"));
        
        if(Greenfoot.isKeyDown("left")) {
            setRotation(getRotation() - 10);
        }        
        if(Greenfoot.isKeyDown("right")) {
            setRotation(getRotation() + 10);
        }
        if(Greenfoot.isKeyDown("space")) {
            fire();
        }        
    }
    
    /**
     * Should the rocket be ignited?
     */
    private void ignite(boolean boosterOn) 
    {
        if(boosterOn) {
            setImage(rocketWithThrust);
            acceleration.setDirection(getRotation());
            increaseSpeed(acceleration);
        }
        else {
            setImage(rocket);        
        }
    }
    
    /**
     * Fire a bullet if the gun is ready.
     */
    private void fire() 
    {
        if(reloadDelayCount >= gunReloadTime) {
            Bullet b = new Bullet(getMovement().copy(), getRotation());
            getWorld().addObject(b, getX(), getY());
            b.move();
            shotsFired++;
            reloadDelayCount = 0;
        }
    }
}