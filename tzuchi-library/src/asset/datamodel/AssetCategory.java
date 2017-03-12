package asset.datamodel;

/**
 * @author Cheng-Hung Chou
 * @since 3/7/2005
 */
public class AssetCategory
{
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
