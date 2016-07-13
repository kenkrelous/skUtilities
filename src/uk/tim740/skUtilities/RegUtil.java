package uk.tim740.skUtilities;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.block.CauldronLevelChangeEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import uk.tim740.skUtilities.util.*;

import javax.annotation.Nullable;

/**
 * Created by tim740 on 22/02/2016
 */
class RegUtil {
    static void regU() {
        Skript.registerExpression(ExprServerIP.class,String.class,ExpressionType.PROPERTY,"[skutil ]ip of server", "[skutil ]server's ip");

        Skript.registerExpression(ExprLoaded.class,Number.class, ExpressionType.PROPERTY,"number of[ loaded] (0¦(commands|cmds)|1¦functions|2¦s(c|k)ripts|3¦triggers|4¦statements|5¦variables|6¦aliases|7¦plugins|8¦addons|9¦events|10¦effects|11¦expressions|12¦conditions)");
        Skript.registerExpression(ExprVersion.class,String.class,ExpressionType.PROPERTY,"%string%'s version", "version of %string%");
        Skript.registerExpression(ExprSysTime.class,Number.class,ExpressionType.PROPERTY,"[current ]system (0¦nanos[econds]|1¦millis[econds]|2¦seconds)");
        Skript.registerExpression(ExprRam.class,Number.class,ExpressionType.PROPERTY,"[skutil ](0¦free|1¦total|2¦max) (ram|memory)");
        Skript.registerExpression(ExprFontNames.class,String.class,ExpressionType.PROPERTY,"[all ][system ]font names");

        Skript.registerEffect(EffDemoMode.class, "send[ fake] trial packet to %player%");
        Skript.registerEffect(EffPrintTag.class, "print (0¦info|1¦warning|2¦error) %string% to console");

        if (Bukkit.getPluginManager().getPlugin("SkQuery") == null){
            Classes.registerClass(new ClassInfo<>(Villager.Profession.class, "profession").parser(new Parser<Villager.Profession>() {
                @Override
                @Nullable
                public Villager.Profession parse(String s, ParseContext context) {
                    try {
                        return Villager.Profession.valueOf(s.toUpperCase());
                    } catch (Exception e) {
                        return null;
                    }
                }

                @Override
                public String toString(Villager.Profession vi, int i) {
                    return vi.name().toLowerCase();
                }
                @Override
                public String toVariableNameString(Villager.Profession vi) {
                    return vi.name().toLowerCase();
                }
                @Override
                public String getVariableNamePattern() {
                    return ".+";
                }
            }));
        }

        Skript.registerEffect(EffVillagerProfession.class, "spawn a %entity% with profession %profession% at %location%");

        Skript.registerEffect(EffSkReloadAliases.class, "skript reload aliases");
        Skript.registerEffect(EffReloadSkript.class, "reload s(k|c)ript %string%");
        Skript.registerEffect(EffRestartServer.class, "re(0¦start|1¦load) server");
        Skript.registerEffect(EffRunOpCmd.class, "(force|make) %player% run (cmd|command) %string% as op");

        Skript.registerExpression(SExprWhitelist.class,OfflinePlayer.class,ExpressionType.PROPERTY,"whitelist");
        Skript.registerEffect(EffToggleWhitelist.class, "turn whitelist (0¦on|1¦off)");
        Skript.registerEffect(EffReloadWhitelist.class, "reload whitelist");
        Skript.registerCondition(CondServerWhitelist.class, "server is whitelisted", "server is(n't| no)t whitelisted");
        Skript.registerCondition(CondPlayerWhitelist.class, "%player% is whitelisted", "%player% is(n't| no)t whitelisted");

        Skript.registerCondition(CondStartsEndsWith.class, "%string% (0¦starts|1¦ends) with %-string%", "%string% does(n't| not) (0¦start|1¦end) with %-string%");

        if(Bukkit.getVersion().contains("(MC: 1.9") ||  Bukkit.getVersion().contains("(MC: 1.1")) {
            Skript.registerExpression(SExprGlideMode.class,Boolean.class,ExpressionType.PROPERTY,"glide (state|ability|mode) of %entity%", "%entity%'s glide (state|ability|mode)");
        }else{
            skUtilities.loadErr("SExprGlideMode");
        }
        regUE();
    }

    private static void regUE() {
        if(Bukkit.getVersion().contains("(MC: 1.9") ||  Bukkit.getVersion().contains("(MC: 1.1")) {
            Skript.registerEvent("CauldronLevelChange", SimpleEvent.class, CauldronLevelChangeEvent.class, "cauldron[ water] level change");
            EventValues.registerEventValue(CauldronLevelChangeEvent.class, Integer.class, new Getter<Integer,CauldronLevelChangeEvent>() {
                @Nullable
                @Override
                public Integer get(CauldronLevelChangeEvent e) {
                    return e.getNewLevel();
                }
            }, 0);
            EventValues.registerEventValue(CauldronLevelChangeEvent.class, Player.class, new Getter<Player, CauldronLevelChangeEvent>(){
                @Nullable
                @Override
                public Player get(CauldronLevelChangeEvent e) {
                    return ((Player) e.getEntity());
                }
            }, 0);
            EventValues.registerEventValue(CauldronLevelChangeEvent.class, Entity.class, new Getter<Entity, CauldronLevelChangeEvent>(){
                @Nullable
                @Override
                public Entity get(CauldronLevelChangeEvent e) {
                    return e.getEntity();
                }
            }, 0);

            Skript.registerEvent("GlideToggle", SimpleEvent.class, EntityToggleGlideEvent.class, "[elytra ]glide toggle");
        }else{
            skUtilities.loadErr("CauldronLevelChange & GlideToggle Events");
        }
    }
}
