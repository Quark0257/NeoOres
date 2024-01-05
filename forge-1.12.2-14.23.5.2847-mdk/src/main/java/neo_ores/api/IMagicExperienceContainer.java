package neo_ores.api;

public interface IMagicExperienceContainer
{
	public void setMagicXp(long value);

	public long getMagicXp();

	public default void addMagicXp(long value)
	{
		setMagicXp(getMagicXp() + value);
	}
}
