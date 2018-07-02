import { Action } from "redux";

abstract class ActionType<TConstructorParameters, TActionPayload>
{
	private readonly typeId: string;
	private readonly mapInputToAction: (parameters: TConstructorParameters) => TActionPayload;

	protected constructor(
		typeId: string,
		mapInputToPayload: (parameters: TConstructorParameters) => TActionPayload)
	{
		this.typeId = typeId;
		this.mapInputToAction = mapInputToPayload;
	}

	public create(p: TConstructorParameters): TActionPayload & Action
	{
		const actionProps = this.mapInputToAction(p) as TActionPayload & Action;

		actionProps.type = this.typeId;

		return actionProps;
	}

	public is(action: Action): action is TActionPayload & Action
	{
		return action.type === this.typeId;
	}
}

// This version of the above suffices for most cases (where TAction === TConstructorParameters)
export class SimpleActionType<TActionPayload = {}> extends ActionType<TActionPayload, TActionPayload>
{
	constructor(typeId: string)
	{
		super(typeId, p => p);
	}
}

export class MappingActionType<TConstructorParameters, TActionPayload> extends ActionType<TConstructorParameters, TActionPayload>
{
	constructor(
		typeId: string,
		mapParametersToPayload: (parameters: TConstructorParameters) => TActionPayload)
	{
		super(typeId, mapParametersToPayload);
	}
}
