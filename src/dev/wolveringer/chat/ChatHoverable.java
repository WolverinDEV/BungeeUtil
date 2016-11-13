package dev.wolveringer.chat;


public class ChatHoverable {

	private final EnumHoverAction action;
	private final IChatBaseComponent value;

	public ChatHoverable(EnumHoverAction enumhoveraction, IChatBaseComponent ichatbasecomponent) {
		this.action = enumhoveraction;
		this.value = ichatbasecomponent;
	}

	public EnumHoverAction getAction() {
		return this.action;
	}

	public IChatBaseComponent getValue() {
		return this.value;
	}

	@Override
	public boolean equals(Object object) {
		if(this == object){
			return true;
		}else if(object != null && this.getClass() == object.getClass()){
			ChatHoverable chathoverable = (ChatHoverable) object;

			if(this.action != chathoverable.action){
				return false;
			}else{
				if(this.value != null){
					if(!this.value.equals(chathoverable.value)){
						return false;
					}
				}else if(chathoverable.value != null){
					return false;
				}

				return true;
			}
		}else{
			return false;
		}
	}

	@Override
	public int hashCode() {
		int i = this.action.hashCode();

		i = 31 * i + (this.value != null ? this.value.hashCode() : 0);
		return i;
	}

	@Override
	public String toString() {
		return "HoverEvent{action=" + this.action + ", value=\'" + this.value + '\'' + '}';
	}
}