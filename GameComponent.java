/**
 * Abstract class representing any component in the game world.
 * Used for polymorphism.
 */
public abstract class GameComponent {
    protected String name;
    
    public GameComponent(String name) {
        this.name = name;
    }
    
    /**
     * Abstract method to inspect the component.
     */
    public abstract void inspect();
    
    /**
     * Get the name of the component.
     */
    public String getName() {
        return name;
    }
    
    @Override
    public String toString() {
        return name;
    }
}

