package com.pro_crafting.mc.wg.blockgenerator;

import org.bukkit.Material;

public class BlockData {
	private Material type;
	private byte dataByte;
	
	public BlockData(Material type, byte dataByte)
	{
		this.type = type;
		this.dataByte = dataByte;
	}

	public Material getType()
	{
		return type;
	}
	
	public byte getDataByte()
	{
		return dataByte;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.dataByte;
		result = prime * result
				+ ((this.type == null) ? 0 : this.type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BlockData other = (BlockData) obj;
		if (this.dataByte != other.dataByte)
			return false;
		if (this.type != other.type)
			return false;
		return true;
	}
}
