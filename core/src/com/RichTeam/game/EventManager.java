package com.RichTeam.game;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A class that maps Object's event strings to actual Java functions.
 */
public class EventManager {
    private final GameScreen game;
    public final HashMap<String, Integer> activityEnergies;
    public final HashMap<String, String> objectInteractions;
    private String[] activities = {"studying", "meet_friends", "eating", "flowers", "town", "shop", "gym", "duck_pond", "library", "east"};
    public static String[] streak_activities = {"studying", "flowers", "town", "shop", "library", "early_bird", "night_owl", "eating"};
    private final Array<String> talkTopics;

    public static HashMap<String, Integer> streaks, daily;
    public boolean catchup_used = false;
    public boolean gotBus = false;
    public boolean breakfast, lunch, dinner = false;

    /**
     * A class that maps Object's event strings to actual Java functions.
     * To run a function call event(eventString), to add arguments add dashes.
     * E.g. a call to the Piazza function with an arg of 1 would be: "piazza-1"
     * Which the function interprets as "study at the piazza for 1 hour".
     * Object's event strings can be set in the Tiled map editor with a property called "event"
     *
     * @param game An instance of the GameScreen containing a player and dialogue box
     */
    public EventManager (GameScreen game) {
        this.game = game;

        // How much energy an hour of each activity should take
        activityEnergies = new HashMap<String, Integer>();
        for (String activity : activities){
            activityEnergies.put(activity, 10);
        }


        // Define what to say when interacting with an object whose text won't change
        objectInteractions = new HashMap<String, String>();
        objectInteractions.put("chest", "Open the chest?");
        objectInteractions.put("comp_sci", "Study in the Computer Science building?");
        objectInteractions.put("rch", "Meet your friends at Ron Cooke Hub?");
        objectInteractions.put("accomodation", "Go to sleep for the night?\nYour alarm is set for 8am.");
        objectInteractions.put("piazza", null); // Changes, dynamically returned in getObjectInteraction
        objectInteractions.put("tree", "Speak to the tree?");
        objectInteractions.put("flowers", "Smell the flowers?");
        objectInteractions.put("shop", "Buy food from the shop?");
        objectInteractions.put("gym", "Work out at the gym?");
        objectInteractions.put("duck_pond", "Feed the ducks?");
        objectInteractions.put("library", "Study at the library?");
        objectInteractions.put("town", "Get the bus to town?");
        objectInteractions.put("east", "Get the bus back to east?");

        // How much energy an hour of each activity should take
        streaks = new HashMap<String, Integer>();
        for (String streak_activity : streak_activities){
            streaks.put(streak_activity, 0);
        }

        // Limits number of times each streak can be increased to 3 times a day
        daily = new HashMap<String, Integer>();
        for (String streak_activity : streak_activities){
            daily.put(streak_activity, 0);
        }

        // Some random topics that can be chatted about
        String[] topics = {"Dogs", "Cats", "Exams", "Celebrities", "Flatmates", "Video games", "Sports", "Food", "Fashion"};
        talkTopics = new Array<String>(topics);
    }

    public void event (String eventKey) throws InterruptedException {
        String[] args = eventKey.split("-");

        // Important functions, most likely called after displaying text
        if (args[0].equals("fadefromblack")) {
            fadeFromBlack();
        } else if (args[0].equals("fadetoblack")) {
            fadeToBlack();
        } else if (args[0].equals("gameover")) {
            game.GameOver();
        }

        // Events related to objects
        switch (args[0]) {
            case "tree":
                String textTree = treeEvent();
                game.dialogueBox.setText(textTree);
                break;
            case "chest":
                String textChest = chestEvent();
                game.dialogueBox.setText(textChest);
                break;
            case "piazza":
                String textPiazza = piazzaEvent(args);
                game.dialogueBox.setText(textPiazza);
                break;
            case "comp_sci":
                String textCompSci = compSciEvent(args);
                game.dialogueBox.setText(textCompSci);
                break;
            case "rch":
                String textRCH = ronCookeEvent(args);
                game.dialogueBox.setText(textRCH);
                break;
            case "accomodation":
                accomEvent(args);
                break;
            case "flowers":
                String textFlowers = flowersEvent(args);
                game.dialogueBox.setText(textFlowers);
                break;
            case "town":
                String textBusStop = busStopEvent(args);
                game.dialogueBox.setText(textBusStop);
                break;
            case "shop":
                String textShop = shopEvent(args);
                game.dialogueBox.setText(textShop);
                break;
            case "gym":
                String textGym = gymEvent(args);
                game.dialogueBox.setText(textGym);
                break;
            case "library":
                String textLibrary = libraryEvent(args);
                game.dialogueBox.setText(textLibrary);
                break;
            case "east":
                townBusStopEvent(args);
                break;
            case "duck_pond":
                String textDuckPond = duckPondEvent(args);
                game.dialogueBox.setText(textDuckPond);
                break;
            case "exit":
                // Should do nothing and just close the dialogue menu
                game.dialogueBox.hide();
                break;
            default:
                String textObject = objectEvent(eventKey);
                game.dialogueBox.setText(textObject);
                break;

        }

    }

    /**
     * Gets the interaction text associated with each object via a key
     * @param key
     * @return The object interaction text
     */
    public String getObjectInteraction(String key) {
        if (key.equals("piazza")) {
            return String.format("Eat %s at the Piazza?", game.getMeal());
        } else {
            return objectInteractions.get(key);
        }
    }

    /**
     * @return True if the object has some custom text to display that isn't just "This is an x!"
     */
    public boolean hasCustomObjectInteraction(String key) {
        return objectInteractions.containsKey(key);
    }

    /**
     * Sets the text when talking to a tree
     */
    public String treeEvent() {
        game.dialogueBox.hideSelectBox();
        //game.dialogueBox.setText("The tree doesn't say anything back.");
        return "The tree doesn't say anything back.";
    }

    /**
     * Sets the text when interacting with the secret area
     */
    public String chestEvent() {
        game.dialogueBox.hideSelectBox();
        //game.dialogueBox.setText("Wow! These barrels is full of so many magical items! I wonder how they will help you out on your journey! Boy, this is an awfully long piece of text, I wonder if someone is testing something?\n...\n...\n...\nHow cool!");
        return "Wow! These barrels is full of so many magical items! I wonder how they will help you out on your journey! Boy, this is an awfully long piece of text, I wonder if someone is testing something?\n...\n...\n...\nHow cool!";
    }

    /**
     * Sets the text when talking to an object without a dedicated function
     */
    public String objectEvent(String object) {
        game.dialogueBox.hideSelectBox();
        //game.dialogueBox.setText("This is a " +  object + "!");
        return "This is a " +  object + "!";
     }

    /**
     * Lets the player meet friends at Ron Cooke Hub for x num of hours, decreases the player's energy and increments the
     * game time.
     *
     * @param args Arguments to be passed, should contain the hours the player wants to meet friends for. E.g. ["piazza", "1"]
     */
    public String ronCookeEvent(String[] args) {
        if (game.getSeconds() > 8*60) {
            int energyCost = activityEnergies.get("meet_friends");
                // If the player is too tired to meet friends
            if (game.getEnergy() < energyCost) {
                return "You are too tired to meet your friends right now!";
            } else if (args.length == 1) {
                // Ask the player to chat about something (makes no difference)
                String[] topics = randomTopics(3);
                game.dialogueBox.getSelectBox().setOptions(topics, new String[]{"rch-"+topics[0], "rch-"+topics[1], "rch-"+topics[2]});
                return "What do you want to chat about?";
            } else {
                // If player conducts activities after 8pm increase night_owl streak
                if (game.getSeconds() > 20*60){
                    // increase player's streak
                    daily.put("night_owl",daily.get("night_owl")+1);
                }
                // Say that the player chatted about this topic for 1-3 hours
                // RNG factor adds a slight difficulty (may consume too much energy to study)
                int hours = ThreadLocalRandom.current().nextInt(1, 4);
                game.decreaseEnergy(energyCost * hours);
                game.passTime(hours * 60); // in seconds
                game.addRecreationalHours(hours);
                return String.format("You talked about %s for %d hours!", args[1].toLowerCase(), hours);
            }
        } else {
            // increase player's streak
daily.put("early_bird",daily.get("early_bird")+1);
            return "It's too early in the morning to meet your friends, go to bed!";
        }
    }

    /**
     * @param amount The amount of topics to return
     * @return An array of x random topics the player can chat about
     */
    private String[] randomTopics(int amount) {
        // Returns an array of 3 random topics
        Array<String> topics = new Array<String>(amount);

        for (int i = 0;i<amount;i++) {
            String choice = talkTopics.random();
            // If statement to ensure topic hasn't already been selected
            if (!topics.contains(choice, false)) {
                topics.add(choice);
            } else {
                i -= 1;
            }
        }

        return topics.toArray(String.class);
    }

    /**
     * The event to be run when interacting with the computer science building
     * Gives the player the option to study for 1, 2 or 3 hours - must go into town to study longer
     * @param args
     */
    public String compSciEvent(String[] args) {
        if (game.getSeconds() > 8*60) {
            int energyCost = activityEnergies.get("studying");
            // If the player is too tired for any studying:
            if (game.getEnergy() < energyCost) {
                game.dialogueBox.hideSelectBox();
                //game.dialogueBox.setText("You are too tired to study right now!");
                return "You are too tired to study right now!";
            } else if (args.length == 1) {
                // If the player has already used their catchup and studied that day, they can't study again
                if (daily.get("studying") >= 1 && catchup_used){
                    game.dialogueBox.hideSelectBox();
                    //game.dialogueBox.setText("You have already studied today!");
                    return "You have already studied today!";
                } else{
                    // If the player has not yet chosen how many hours, ask
                    //game.dialogueBox.setText("Study for how long?");
                    game.dialogueBox.getSelectBox().setOptions(new String[]{"1 Hour (10)", "2 Hours (20)", "3 Hours (30)"}, new String[]{"comp_sci-1", "comp_sci-2", "comp_sci-3"});
                    return "Study for how long?";
                }
            } else {
                int hours = Integer.parseInt(args[1]);
                // If the player does not have enough energy for the selected hours
                if (game.getEnergy() < hours*energyCost) {
                    //game.dialogueBox.setText("You don't have the energy to study for this long!");
                    return "You don't have the energy to study for this long!";
                } else {
                    // If they do have the energy to study increase streaks and complete event
                    if (game.getSeconds() > 20*60){
                        if (daily.get("night_owl") < 3){
                            // increase player's streak
                            daily.put("night_owl",daily.get("night_owl")+1);
                        }
                    }
                    // increase player's streak
                    daily.put("studying",daily.get("studying")+1);
                    //game.dialogueBox.setText(String.format("You studied for %s hours!\nYou lost %d energy", args[1], hours*energyCost));
                    game.decreaseEnergy(energyCost * hours);
                    game.addStudyHours(hours);
                    game.addStudyTimes();
                    if (daily.get("studying") > 1){
                        catchup_used = GameScreen.useCatchup(catchup_used);
                    }
                    game.passTime(hours * 60); // in seconds
                    return String.format("You studied for %s hours!\nYou lost %d energy", args[1], hours*energyCost);
                }
            }
        } else {
            //game.dialogueBox.setText("It's too early in the morning to study, go to bed!");
            if (daily.get("early_bird") < 3 && game.getSeconds() > 0){
                // increase player's streak
                
                daily.put("early_bird",daily.get("early_bird")+1);
            }
            return "It's too early in the morning to study, go to bed!";
        }
    }


    /**
     * The event to be run when the player interacts with the Piazza
     * Gives the player the choice to eat breakfast, lunch or dinner depending on the time of day
     * @param args
     */
    public String piazzaEvent(String[] args) {
        if (game.getSeconds() > 8*60) {
            int energyCost = activityEnergies.get("eating");
            if (game.getEnergy() < energyCost) {
                //game.dialogueBox.setText("You are too tired to eat right now!");
                return "You are too tired to eat right now!";
            } else {
                if (game.getSeconds() > 20*60){
                    // increase player's streak
                    daily.put("night_owl",daily.get("night_owl")+1);
                }
                if (daily.get("eating") == 3){
                    // increase player's streak
                    streaks.put("eating", streaks.getOrDefault("eating", 0) + 1);
                } else if (daily.get("eating") < 3){
                    // increase player's eating for the day streak if under limit
                    daily.put("eating",daily.get("eating")+1);
                }
                game.decreaseEnergy(energyCost);
                game.passTime(60); // in seconds
                return String.format("You took an hour to eat %s at the Piazza!\nYou lost 10 energy!", game.getMeal());
            }
        } else {
            //game.dialogueBox.setText("It's too early in the morning to eat food, go to bed!");
            if (game.getSeconds() > 0){
                // increase player's streak
                daily.put("early_bird",daily.get("early_bird")+1);
            }
            return "It's too early in the morning to eat food, go to bed!";
        }

    }

    /**
     * The event to be run when interacting with the flowerbed bench
     * Gives the player the option to study for 1, 2 or 3 hours
     * @param args
     */
    public String flowersEvent(String[] args) {
        if (game.getSeconds() > 8*60) {
            int energyCost = activityEnergies.get("flowers");
            // If the player is too tired to smell the flowers
            if (game.getEnergy() < energyCost) {
                game.dialogueBox.hideSelectBox();
                //game.dialogueBox.setText("You are too tired to smell the flowers right now!");
                return "You are too tired to smell the flowers right now!";
            } else if (args.length == 1) {
                // If the player has not yet chosen how many hours, ask
                //game.dialogueBox.setText("Smell the flowers for how long?");
                game.dialogueBox.getSelectBox().setOptions(new String[]{"1 Hour (10)", "2 Hours (20)", "3 Hours (30)"}, new String[]{"flowers-1", "flowers-2", "flowers-3"});
                return "Smell the flowers for how long?";
            } else {
                int hours = Integer.parseInt(args[1]);
                // If the player does not have enough energy for the selected hours
                if (game.getEnergy() < hours*energyCost) {
                    //game.dialogueBox.setText("What if you fell asleep? You don't have the energy!");
                    return "What if you fell asleep? You don't have the energy!";
                } else {
                    // If they do have the energy to smell the flowers
                    if (game.getSeconds() > 20*60){
                        // increase player's streak
                        daily.put("night_owl",daily.get("night_owl")+1);
                    }
                    // increase player's streak
                    daily.put("flowers",daily.get("flowers")+1);
                    //game.dialogueBox.setText(String.format("You smelled the flowers for %s hours!\nYou lost %d energy", args[1], hours*energyCost));
                    game.decreaseEnergy(energyCost * hours);
                    game.passTime(hours * 60); // in seconds
                    game.addRecreationalHours(hours);
                    return String.format("You smelled the flowers for %s hours!\nYou lost %d energy", args[1], hours*energyCost);
                }
            }
        } else {
            //game.dialogueBox.setText("It's too early in the morning to smell the flowers, go to bed!");
            // increase player's streak
            daily.put("early_bird",daily.get("early_bird")+1);
            return "It's too early in the morning to smell the flowers, go to bed!";
        }
    }

    /**
     * The event to be run when interacting with the bus stop to town
     * @param args
     */
    public String busStopEvent(String[] args) throws InterruptedException {
        if (game.getSeconds() > 8*60) {
            //Still need an energy cost to prevent player from going into town when they have no energy
            int energyCost = activityEnergies.get("town");
            // If the player is too tired for any travelling:
            if (game.getEnergy() < energyCost) {
                game.dialogueBox.hideSelectBox();
                //game.dialogueBox.setText("You are too tired to get the bus right now!");
                return "You are too tired to get the bus right now!";
            } else if (args.length == 1) {
                // Player goes into town for undefined amount of time so no need to ask
                // If the player does not have enough energy for at least one hour
                if (game.getEnergy() < energyCost) {
                    //game.dialogueBox.setText("You don't have the energy to go into town right now!");
                    return "You don't have the energy to go into town right now!";
                } else {
                    // increase player's streak
                    daily.put("town",daily.get("town")+1);
                    // If they do have the energy to go into town
                    HustleGame.setMap();
                    //game.dialogueBox.setText("You got the bus into town.");
                    return "You got the bus into town.";
                }
            }
        } else {
            //game.dialogueBox.setText("It's too early in the morning to go into town, there are no buses yet!");
            // increase player's streak
            daily.put("early_bird",daily.get("early_bird")+1);
            return "It's too early in the morning to go into town, there are no buses yet!";
        }
        return null;
    }

    /**
     * The event to be run when interacting with the shop
     * Gives the player the option to buy food for 1, 2 or 3 hours
     * @param args
     */
    public String shopEvent(String[] args) {
        if (game.getSeconds() > 8*60) {
            int energyCost = activityEnergies.get("shop");
            // If the player has the energy to eat
            if (game.getEnergy() < energyCost) {
                //game.dialogueBox.setText("You are too tired to eat right now!");
                return "You are too tired to eat right now!";
            } else {
                if (game.getSeconds() > 20*60){
                    daily.put("night_owl",daily.get("night_owl")+1);
                }
                if (daily.get("eating") == 3){
                    // increase player's streak
                    streaks.put("eating", streaks.getOrDefault("eating", 0) + 1);
                } else if (daily.get("eating") < 3){
                    // increase player's eating for the day streak if under limit
                    daily.put("eating",daily.get("eating")+1);
                }
                daily.put("shop",daily.get("shop")+1);
                game.decreaseEnergy(energyCost);
                game.passTime(60); // in seconds
                return String.format("You took an hour to buy and eat %s at nisa!\nYou lost %d energy!", game.getMeal(), energyCost);
            }
        } else {
            //game.dialogueBox.setText("It's too early in the morning to eat food, go to bed!");
            if (game.getSeconds() > 0){
                daily.put("early_bird",daily.get("early_bird")+1);
            }
            return "It's too early in the morning to eat food, go to bed!";
        }
    }

    /**
     * Lets the player go to sleep, fades the screen to black then shows a dialogue about the amount of sleep
     * the player gets
     * Then queues up fadeFromBlack to be called when this dialogue closes
     * @see GameScreen fadeToBlack function
     * @param args Unused currently
     */
    public void accomEvent(String[] args) {
        game.setSleeping(true);
        game.dialogueBox.hide();

        // Calculate the hours slept to the nearest hour
        // Wakes the player up at 8am
        float secondsSlept;
        if (game.getSeconds() < 60*8) {
            secondsSlept = (60*8 - game.getSeconds());
        } else {
            // Account for the wakeup time being in the next day
            secondsSlept = (((60*8) + 1440) - game.getSeconds());
        }
        int hoursSlept = Math.round(secondsSlept / 60f);

        RunnableAction setTextAction = new RunnableAction();
        setTextAction.setRunnable(new Runnable() {
            @Override
            public void run() {
                if (game.getSleeping()) {
                    game.dialogueBox.show();
                    game.dialogueBox.setText(String.format("You slept for %d hours!\nYou recovered %d energy!", hoursSlept, Math.min(100, hoursSlept*13)), "fadefromblack");
                    // Restore energy and pass time
                    game.setEnergy(hoursSlept*13);
                    game.passTime(secondsSlept);
                    game.addSleptHours(hoursSlept);
                }
            }
        });

        fadeToBlack(setTextAction);
    }

    //Town POIs

    /**
     * The event to be run when interacting with the gym
     * Gives the player the option to buy food for 2, 3 or 4 hours
     * @param args
     */
    public String gymEvent(String[] args) {
        if (game.getSeconds() > 8*60) {
            int energyCost = activityEnergies.get("gym");
            // If the player is too tired for any travelling:
            if (game.getEnergy() < energyCost) {
                game.dialogueBox.hideSelectBox();
                //game.dialogueBox.setText("You are too tired to go to the gym right now!");
                return "You are too tired to go to the gym right now!";
            } else if (args.length == 1) {
                // If the player has not yet chosen how many hours, ask
                //game.dialogueBox.setText("Work out for how long?");
                game.dialogueBox.getSelectBox().setOptions(new String[]{"2 Hours (20)", "3 Hours (30)", "4 Hours (40)"}, new String[]{"gym-2", "gym-3", "gym-4"});
                return "Work out for how long?";
            } else {
                int hours = Integer.parseInt(args[1]);
                // If the player does not have enough energy for the selected hours
                if (game.getEnergy() < hours*energyCost) {
                    //game.dialogueBox.setText("You don't have the energy to go to work out right now! Head back to east!");
                    return "You don't have the energy to go to work out right now! Head back to east!";
                } else {
                    // If they do have the energy to work out
                    if (game.getSeconds() > 20*60){
                        daily.put("night_owl",daily.get("night_owl")+1);
                    }
                    //game.dialogueBox.setText(String.format("You spent %s hours working out at the gym.\nYou lost %d energy", args[1], hours*energyCost));
                    game.decreaseEnergy(energyCost * hours);
                    game.addRecreationalHours(hours);
                    game.passTime(hours * 60); // in seconds
                    return String.format("You spent %s hours working out at the gym!\nYou lost %d energy", args[1], hours*energyCost);
                }
            }
        } else {
            //game.dialogueBox.setText("It's too early to work out, the gym's not open yet!");
            if (game.getSeconds() > 0){
                daily.put("early_bird",daily.get("early_bird")+1);
            }
            return "It's too early to work out, the gym's not open yet!";
        }
    }

    /**
     * The event to be run when interacting with the duck pond
     * Gives the player the option to feed the ducks for 1, 2 or 3 hours
     * @param args
     */
    public String duckPondEvent(String[] args) {
        if (game.getSeconds() > 8*60) {
            int energyCost = activityEnergies.get("duck_pond");
            // If the player is too tired for any travelling:
            if (game.getEnergy() < energyCost) {
                game.dialogueBox.hideSelectBox();
                //game.dialogueBox.setText("You are too tired to feed the ducks right now!");
                return "You are too tired to feed the ducks right now!";
            } else if (args.length == 1) {
                // If the player has not yet chosen how many hours, ask
                //game.dialogueBox.setText("Feed the ducks for how long?");
                game.dialogueBox.getSelectBox().setOptions(new String[]{"1 Hour (10)", "2 Hours (20)", "3 Hours (30)"}, new String[]{"duck_pond-1", "duck_pond-2", "duck_pond-3"});
                return "Feed the ducks for how long?";
            } else {
                int hours = Integer.parseInt(args[1]);
                // If the player does not have enough energy for the selected hours
                if (game.getEnergy() < hours*energyCost) {
                    //game.dialogueBox.setText("You don't have the energy to feed the ducks right now! Head back to east!");
                    return "You don't have the energy to feed the ducks right now! Head back to east!";
                } else {
                    // If they do have the energy to feed the ducks
                    if (game.getSeconds() > 20*60){
                        daily.put("night_owl",daily.get("night_owl")+1);
                    }
                    //game.dialogueBox.setText(String.format("You spent %s hours feeding the ducks.\nYou lost %d energy", args[1], hours*energyCost));
                    game.decreaseEnergy(energyCost * hours);
                    game.passTime(hours * 60); // in seconds
                    game.addRecreationalHours(hours);
                    return String.format("You spent %s hours feeding the ducks!\nYou lost %d energy", args[1], hours*energyCost);
                }
            }
        } else {
            //game.dialogueBox.setText("It's too early to feed the ducks, they're not hungry!");
            if (game.getSeconds() > 0){
                daily.put("early_bird",daily.get("early_bird")+1);
            }
            return "It's too early to feed the ducks, they're not hungry!";
        }
    }

    /**
     * The event to be run when interacting with the library
     * Gives the player the option to study for 2, 3 or 4 hours - must go into town to study longer
     * @param args
     */
    public String libraryEvent(String[] args) {
        if (game.getSeconds() > 8*60) {
            int energyCost = activityEnergies.get("library");
            // If the player is too tired for any studying:
            if (game.getEnergy() < energyCost) {
                game.dialogueBox.hideSelectBox();
                //game.dialogueBox.setText("You are too tired to study at the library right now!");
                return "You are too tired to study at the library right now!";
            } else if (args.length == 1) {
                // If the player has already used their catchup and studied that day, they can't study again
                if (daily.get("studying") >= 1 && catchup_used){
                    game.dialogueBox.hideSelectBox();
                    //game.dialogueBox.setText("You have already studied today!");
                    return "You have already studied today!";
                }else{
                    // If the player has not yet chosen how many hours, ask
                    //game.dialogueBox.setText("Study for how long?");
                    game.dialogueBox.getSelectBox().setOptions(new String[]{"2 Hours (20)", "3 Hours (30)", "4 Hours (40)"}, new String[]{"library-2", "library-3", "library-4"});
                    return "Study for how long?";
                }
            } else {
                int hours = Integer.parseInt(args[1]);
                // If the player does not have enough energy for the selected hours
                if (game.getEnergy() < hours*energyCost) {
                    //game.dialogueBox.setText("You don't have the energy to study for this long! Head back to east!");
                    return "You don't have the energy to study for this long! Head back to east!";
                } else {
                    // If they do have the energy to study
                    daily.put("studying",daily.get("studying")+1);
                    daily.put("library",daily.get("library")+1);
                    if (game.getSeconds() > 20*60){
                        daily.put("night_owl",daily.get("night_owl")+1);
                    }
                    //game.dialogueBox.setText(String.format("You studied for %s hours!\nYou lost %d energy", args[1], hours*energyCost));
                    game.decreaseEnergy(energyCost * hours);
                    game.addStudyHours(hours);
                    game.addStudyTimes();
                    if (daily.get("studying") > 1){
                        catchup_used = GameScreen.useCatchup(catchup_used);
                    }
                    game.passTime(hours * 60); // in seconds
                    return String.format("You studied for %s hours!\nYou lost %d energy", args[1], hours*energyCost);
                }
            }
        } else {
            //game.dialogueBox.setText("It's too early in the morning to study, go to bed!");
            daily.put("early_bird",daily.get("early_bird")+1);
            return "It's too early in the morning to study, go to bed!";
        }
    }

    /**
     * The event to be run when interacting with the bus stop back to east
     * @param args
     */
    public void townBusStopEvent(String[] args) throws InterruptedException {
        // Player must be able to get back to east at any time in the game, so energy and time of day are irrelevant
        gotBus = true;
        HustleGame.setMap();
        game.dialogueBox.setText("You got the bus back to east.");
    }

    /**
     * gets the Players current actvity streaks in the game to be used for gaining acheivements
     * @return
     */

    public static HashMap<String, Integer> getStreaks(){
        return streaks;
    }



    /**
     * Fades the screen to black
     */
    public void fadeToBlack() {
        game.blackScreen.addAction(Actions.fadeIn(3f));
    }

    /**
     * Fades the screen to black, then runs a runnable after it is done
     * @param runnable A runnable to execute after fading is finished
     */
    /**
     * Fades the screen to black, executes runnable action after fadde
     * @ param runnable the runnable exection to be executed
     */
    public void fadeToBlack(RunnableAction runnable) {
        game.blackScreen.addAction(Actions.sequence(Actions.fadeIn(3f), runnable));
    }

    /**
     * Fades the screen back in from black, displays a good morning message if the player was sleeping
     */
    public void fadeFromBlack() {
        // If the player is sleeping, queue up a message to be sent
        if (game.getSleeping()) {
            RunnableAction setTextAction = new RunnableAction();
            setTextAction.setRunnable(new Runnable() {
                @Override
                public void run() {
                    if (game.getSleeping()) {
                        game.dialogueBox.show();
                        // Show a text displaying how many days they have left in the game
                        game.dialogueBox.setText(game.getWakeUpMessage());
                        game.setSleeping(false);
                    }
                }
            });

            // Queue up events
            game.blackScreen.addAction(Actions.sequence(Actions.fadeOut(3f), setTextAction));
        } else {
            game.blackScreen.addAction(Actions.fadeOut(3f));
        }
    }
}
