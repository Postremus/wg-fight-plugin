package de.pro_crafting.wg;

import de.pro_crafting.wg.arena.Arena;

public class PlayerGroup {
	private Arena arena;
	private PlayerRole role;
	
	public PlayerGroup(Arena arena, PlayerRole role) {
		this.arena = arena;
		this.role = role;
	}
	
	public Arena getArena() {
		return this.arena;
	}
	
	public PlayerRole getRole() {
		return this.role;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.arena == null) ? 0 : this.arena.hashCode());
		result = prime * result
				+ ((this.role == null) ? 0 : this.role.hashCode());
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
		PlayerGroup other = (PlayerGroup) obj;
		if (this.arena == null) {
			if (other.arena != null)
				return false;
		} else if (!this.arena.equals(other.arena))
			return false;
		if (this.role != other.role)
			return false;
		return true;
	}
}
