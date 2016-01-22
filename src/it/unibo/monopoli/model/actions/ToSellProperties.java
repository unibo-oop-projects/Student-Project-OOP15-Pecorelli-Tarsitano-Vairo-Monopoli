package it.unibo.monopoli.model.actions;

import java.util.Objects;

import it.unibo.monopoli.model.mainunits.Player;
import it.unibo.monopoli.model.table.Building;
import it.unibo.monopoli.model.table.Land;
import it.unibo.monopoli.model.table.LandGroup;
import it.unibo.monopoli.model.table.Ownership;

/**
 * This class represent one of the {@link MoneyAction}s of the game. This one
 * allows to sell {@link Ownership}s and/or {@link Building}s.
 *
 */
public class ToSellProperties extends ToBuyAndSellProperties {

    private final Ownership ownership;

    /**
     * This is a static method that can be used to create a new instance of this
     * class.
     * 
     * @param amount
     *            - the amount of the sale
     * @param ownership
     *            - the {@link Ownership} to sell
     */
    protected ToSellProperties(final int amount, final Ownership ownership) {
        super(amount);
        this.ownership = ownership;
    }

    /**
     * This is a static method that can be used to create a new instance of this
     * class.
     * 
     * @param amount
     *            - the amount of the sale
     * @param land
     *            - the {@link Land} on which the {@link Building} was built
     * @param building
     *            - the {@link Building} to sell
     */
    protected ToSellProperties(final int amount, final Land land, final Building building) {
        super(amount, building);
        this.ownership = land;
    }

    /**
     * This is a static method that can be used to create a new instance of this
     * class.
     * 
     * @param amount
     *            - the amount of the sale
     * @param ownership
     *            - the {@link Ownership} to sell
     * @return an instance of this class
     * @throws NullPointerException
     *             - if instead of an {@link Ownership} there is null
     * @throws IllegalArgumentException
     *             - if the amount is less than or equal to zero
     */
    public static ToSellProperties buyAOwnership(final int amount, final Ownership ownership) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Only positive amount different of zero!");
        }
        return new ToSellProperties(amount, Objects.requireNonNull(ownership));
    }

    /**
     * This is a static method that can be used to create a new instance of this
     * class.
     * 
     * @param amount
     *            - the amount of the sale
     * @param land
     *            - the {@link Land} on which the {@link Building} was built
     * @param building
     *            - the {@link Building} to sell
     * @return an instance of this class
     * @throws NullPointerException
     *             - if instead of an {@link Land} and/or a {@link Building}
     *             there are some null
     * @throws IllegalArgumentException
     *             - if the amount is less than or equal to zero
     * @throws IllegalArgumentException
     *             - if the {@link Land} is mortgage]
     * @throws IllegalArgumentException
     *             - if the {@link Player} doesn't own all the {@link Land}s of
     *             this specific {@link LandGroup}
     */
    public static ToSellProperties buyABuilding(final int amount, final Land land, final Building building) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Only positive amount different of zero!");
        }
        for (final Ownership o : land.getGroup().getMembers()) {
            if (o.isMortgaged()) {
                throw new IllegalArgumentException("Can build only in a NOT mortgage group of land");
            }
        }
        if (!((Player) land.getOwner()).getOwnerships().containsAll(land.getGroup().getMembers())) {
            throw new IllegalArgumentException("Can build only if the Player has ALL the lands of this land's group");
        }
        return new ToSellProperties(amount, Objects.requireNonNull(land), Objects.requireNonNull(building));
    }

    @Override
    protected void whatToDoWithBuilding(final Building building) {
        ((LandGroup) this.ownership.getGroup()).removeBuilding(building);
    }

    @Override
    protected void whatToDoWithOwnership(final Player player) {
        player.removeOwnership(this.ownership);
    }

}
