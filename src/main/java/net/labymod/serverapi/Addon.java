package net.labymod.serverapi;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Getter;

import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * An Addon represents a player's addon
 * The addons are being sent when a user joins the server
 * You can retrieve them by using LabyModPlayerJoinEvent#getAddons()
 *
 * @author Jan
 */

@Getter
public class Addon {

    private UUID uuid;
    private String name;

    @ConstructorProperties({"uuid", "name"})
    public Addon(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    /**
     * Parses the addons from the INFO plugin message
     *
     * @param jsonObject the json object of the message
     * @return a list containing the message's addons
     */
    public static List<Addon> getAddons(JsonObject jsonObject ) {
        if ( !jsonObject.has( "addons" ) || !jsonObject.get( "addons" ).isJsonArray() )
            return new ArrayList<Addon>();

        List<Addon> addons = new ArrayList<Addon>();

        for ( JsonElement arrayElement : jsonObject.get( "addons" ).getAsJsonArray() ) {
            if ( !arrayElement.isJsonObject() )
                continue;

            JsonObject arrayObject = arrayElement.getAsJsonObject();

            if ( !arrayObject.has( "uuid" ) || !arrayObject.get( "uuid" ).isJsonPrimitive() || !arrayObject.get( "uuid" ).getAsJsonPrimitive().isString()
                    || !arrayObject.has( "name" ) || !arrayObject.get( "name" ).isJsonPrimitive() || !arrayObject.get( "name" ).getAsJsonPrimitive().isString() )
                continue;

            UUID uuid = null;

            try {
                uuid = UUID.fromString( arrayObject.get( "uuid" ).getAsString() );
            } catch ( IllegalArgumentException ex ) {
                continue;
            }

            addons.add( new Addon( uuid, arrayObject.get( "name" ).getAsString() ) );
        }

        return addons;
    }

}