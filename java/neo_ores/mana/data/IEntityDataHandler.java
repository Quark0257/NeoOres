package neo_ores.mana.data;

import java.util.Map;

public interface IEntityDataHandler 
{
	Map<String,Float> writeToMap();
	
	void writeToFile(Map<String,Float> map);
}
