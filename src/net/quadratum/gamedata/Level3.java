package net.quadratum.gamedata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.quadratum.core.Action;
import net.quadratum.core.Core;
import net.quadratum.core.GameStats;
import net.quadratum.core.MapData;
import net.quadratum.core.MapPoint;
import net.quadratum.core.Piece;
import net.quadratum.core.Player;
import net.quadratum.core.PlayerInformation;
import net.quadratum.core.Unit;
import net.quadratum.core.WinCondition;

public class Level3 implements Level {
       
        public Level3() {}

        public String getMap() {
                return new String("maps/level3.qmap");
        }
       
        public int getStartingResources() {
                return 1000;
        }
       
        public int getMaxUnits() {
                return 4;
        }
       
        public Player getAI() {
                return new Level3AI();
        }
       
        public WinCondition getWinCondition() {
                return new Level3WinCondition();
        }
       
        public ArrayList<Piece>[] getPieces() {
                ArrayList<Piece>[] pieces = (ArrayList<Piece>[])new ArrayList[2];
                pieces[0] = DefaultPieces.getPieces();
                pieces[1] = DefaultPieces.getPieces();
                return pieces;
        }
       
        class Level3AI implements Player {
                // TODO make units patrol
                Core _core;
                Map<MapPoint, Integer> _units;
                Object _lockObject;

                public Level3AI() {
                        _lockObject = new Object();
                }

                public void start(Core core, MapData mapData, int id, int totalPlayers) {
                        _core = core;
                       
                        // Place units
                        placeScout(new MapPoint(19,10));
                        placeRanger(new MapPoint(19,11));
                        placeSoldier(new MapPoint(20,10));
                        placeTank(new MapPoint(20,11));
                        _core.ready(this);
                }
               
               
                // Places a scout
                private void placeScout(MapPoint location) {
                        int unit = _core.placeUnit(this, location, new String("Scout"));
                        _core.updateUnit(this, unit, 6, new MapPoint(1,0));
                        _core.updateUnit(this, unit, 1, new MapPoint(5,0));
                        _core.updateUnit(this, unit, 0, new MapPoint(6,0));
                        _core.updateUnit(this, unit, 3, new MapPoint(0,1));
                        _core.updateUnit(this, unit, 4, new MapPoint(3,1));
                        _core.updateUnit(this, unit, 3, new MapPoint(1,3));
                        _core.updateUnit(this, unit, 4, new MapPoint(5,3));
                        _core.updateUnit(this, unit, 1, new MapPoint(0,5));
                        _core.updateUnit(this, unit, 1, new MapPoint(2,5));
                        _core.updateUnit(this, unit, 4, new MapPoint(3,5));
                        _core.updateUnit(this, unit, 1, new MapPoint(5,5));
                        _core.updateUnit(this, unit, 0, new MapPoint(6,5));
                }
               
                // Places a ranger
                private void placeRanger(MapPoint location) {
                        int unit = _core.placeUnit(this, location, new String("Ranger"));
                        _core.updateUnit(this, unit, 6, new MapPoint(0,0));
                        _core.updateUnit(this, unit, 2, new MapPoint(5,0));
                        _core.updateUnit(this, unit, 2, new MapPoint(2,1));
                        _core.updateUnit(this, unit, 4, new MapPoint(6,2));
                        _core.updateUnit(this, unit, 4, new MapPoint(0,3));
                        _core.updateUnit(this, unit, 1, new MapPoint(2,3));
                        _core.updateUnit(this, unit, 0, new MapPoint(5,3));
                        _core.updateUnit(this, unit, 5, new MapPoint(7,4));
                        _core.updateUnit(this, unit, 1, new MapPoint(0,5));
                        _core.updateUnit(this, unit, 0, new MapPoint(1,5));
                        _core.updateUnit(this, unit, 0, new MapPoint(5,5));
                        _core.updateUnit(this, unit, 4, new MapPoint(3,6));
                }
               
                // Places a soldier
                private void placeSoldier(MapPoint location) {
                        int unit = _core.placeUnit(this, location, new String("Soldier"));
                        _core.updateUnit(this, unit, 0, new MapPoint(1,0));
                        _core.updateUnit(this, unit, 2, new MapPoint(3,0));
                        _core.updateUnit(this, unit, 4, new MapPoint(6,0));
                        _core.updateUnit(this, unit, 0, new MapPoint(0,1));
                        _core.updateUnit(this, unit, 5, new MapPoint(5,2));
                        _core.updateUnit(this, unit, 4, new MapPoint(6,2));
                        _core.updateUnit(this, unit, 0, new MapPoint(0,3));
                        _core.updateUnit(this, unit, 1, new MapPoint(2,3));
                        _core.updateUnit(this, unit, 4, new MapPoint(6,4));
                        _core.updateUnit(this, unit, 0, new MapPoint(0,5));
                        _core.updateUnit(this, unit, 4, new MapPoint(2,6));
                        _core.updateUnit(this, unit, 6, new MapPoint(4,6));
                }
               
                // Places a tank
                private void placeTank(MapPoint location) {
                        int unit = _core.placeUnit(this, location, new String("Tank"));
                        _core.updateUnit(this, unit, 4, new MapPoint(0,0));
                        _core.updateUnit(this, unit, 4, new MapPoint(2,0));
                        _core.updateUnit(this, unit, 0, new MapPoint(4,0));
                        _core.updateUnit(this, unit, 0, new MapPoint(6,0));
                        _core.updateUnit(this, unit, 3, new MapPoint(1,2));
                        _core.updateUnit(this, unit, 1, new MapPoint(5,3));
                        _core.updateUnit(this, unit, 4, new MapPoint(6,3));
                        _core.updateUnit(this, unit, 2, new MapPoint(0,4));
                        _core.updateUnit(this, unit, 4, new MapPoint(0,6));
                        _core.updateUnit(this, unit, 4, new MapPoint(2,6));
                        _core.updateUnit(this, unit, 6, new MapPoint(4,6));
                }
               
                public void updatePieces(List<Piece> pieces) {}
               
                public void end(GameStats stats) {}
               
                public void lost() {}
               
                public void turnStart() {
                        synchronized(_lockObject)
                        {
                                Map<MapPoint, Action.ActionType> valid;
                                Set<MapPoint> keys = _units.keySet();
                                for(MapPoint key : keys)
                                {
                                        valid = _core.getValidActions(this, _units.get(key).intValue());
                                        if(valid == null)
                                        {
                                                continue;
                                        }
                                        for(MapPoint point : valid.keySet())
                                        {
                                                if(valid.get(point) == Action.ActionType.ATTACK)
                                                {
                                                        _core.unitAction(this, _units.get(key), point);
                                                        continue;
                                                }
                                        }
                                }
                                _core.endTurn(this);
                        }
                }

                public void updateMapData(MapData mapData) {}

                public void updateMap(Map<MapPoint,Integer> units, Set<MapPoint> sight,
                                Action lastAction) {
                        synchronized(_lockObject)
                        {
                                _units = new HashMap<MapPoint, Integer>();
                                for(MapPoint key : units.keySet())
                                {
                                        if(_core.getUnit(this, units.get(key).intValue())._owner == 1)
                                        {
                                                _units.put(new MapPoint(key), new Integer(units.get(key)));
                                        }
                                }
                        }
                }

                public void chatMessage(int from, String message) {}

                public void updateTurn(int id) {}
        }

        class Level3WinCondition implements WinCondition {
                public boolean hasPlayerWon(Map<MapPoint, Unit> units, PlayerInformation playerInformation, int playerNumber)
                {
                        return false;
                }
               
                public boolean hasPlayerLost(Map<MapPoint, Unit> units, PlayerInformation playerInformation, int playerNumber)
                {
                        return units.size() == 0;
                }
               
                public String getObjectives()
                {
                        return new String("Kill the enemy units!");
                }
        }
}