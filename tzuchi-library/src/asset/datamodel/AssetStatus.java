package asset.datamodel;

/**
 * @author Cheng-Hung Chou
 * @since 3/7/2005
 */
public class AssetStatus
{
   public static final int AVAILABLE = 1;
   public static final int CHECK_OUT = 2;
   public static final int OVER_DUE = 3;
   public static final int MISSING = 4;
   
   private int id = -1;
   private String name = "";
   
   /**
    * @return Returns the id.
    */
   public int getId()
   {
      return id;
   }
   
   /**
    * @param id The id to set.
    */
   public void setId(int id)
   {
      this.id = id;
   }
   
   /**
    * @return Returns the name.
    */
   public String getName()
   {
      return name;
   }
   
   /**
    * @param name The name to set.
    */
   public void setName(String name)
   {
      this.name = name;
   }
   
   public String toString()
   {
      return name;
   }
}
