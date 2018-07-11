import { Action } from "redux";

/**
 * Encapsulates the type ID and provides methods for creating actions or checking if they are of this type.
 *
 * @remarks
 * We distinguish between:
 *
 * # Constructor parameters (ie to the inputs to an action creator)
 * # Action payloads (ie all the stuff on an action other than the type field)
 *
 * In reality, these are usually the same
 */
export abstract class ActionType<TConstructorParameters, TActionPayload>
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

/** The most common action type, where the construction arguments are the same as the action payload */
export class SimpleActionType<TActionPayload = {}> extends ActionType<TActionPayload, TActionPayload>
{
	constructor(typeId: string)
	{
		super(typeId, p => p);
	}
}

/** Takes a func to map between constructor parameters and the action payload - ie, an action creator */
export class MappingActionType<TConstructorParameters, TActionPayload> extends ActionType<TConstructorParameters, TActionPayload>
{
	constructor(
		typeId: string,
		mapParametersToPayload: (parameters: TConstructorParameters) => TActionPayload)
	{
		super(typeId, mapParametersToPayload);
	}
}
