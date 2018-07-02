import { Action } from "redux";

export default abstract class ActionType<TAction, TConstructorParameters = TAction>
{
	private readonly typeId: string;

	protected constructor(typeId: string)
	{
		this.typeId = typeId;
	}

	public create(p: TConstructorParameters): TAction & Action
	{
		const actionProps = this.createPayload(p) as TAction & Action;

		actionProps.type = this.typeId;

		return actionProps;
	}

	public is(action: Action): action is TAction & Action
	{
		return action.type === this.typeId;
	}

	protected abstract createPayload(p: TConstructorParameters): TAction;
}

// This version of the above suffices for most cases (where TAction === TConstructorParameters)
export class SimpleActionType<TAction = {}> extends ActionType<TAction>
{
	constructor(typeId: string) { super(typeId); }

	// in the simple case, the arguments to the action's constructor ARE the payload
	protected createPayload(parameters: TAction): TAction { return parameters; }
}
