package com.skloch.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * A class that maps Object's event strings to actual Java functions.
 */
public class EventManager {
    private final GameScreen game;
    public final HashMap<String, Integer> activityEnergies;
    public final HashMap<String, String> objectInteractions;
    private final Array<String> talkTopics;

    public static HashMap<String, Integer> streaks;
    public boolean catchup_used = false;
    public int daily_study = 0;
    public boolean gotBus = false;

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
        activityEnergies.put("studying", 10);
        activityEnergies.put("meet_friends", 10);
        activityEnergies.put("eating", 10);
        activityEnergies.put("flowers", 10);
        activityEnergies.put("town", 10);
        activityEnergies.put("shop", 10);
        activityEnergies.put("gym", 10);
        activityEnergies.put("library", 10);
        activityEnergies.put("east", 10);


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
        objectInteractions.put("library", "Study at the library?");
        objectInteractions.put("town", "Get the bus to town?");
        objectInteractions.put("east", "Get the bus back to east?");

        // How much energy an hour of each activity should take
        streaks = new HashMap<String, Integer>();
        streaks.put("eating", 0);
        streaks.put("flowers", 0);
        streaks.put("town", 0);
        streaks.put("shop", 0);
        streaks.put("determined", 0); //try to do activities without energy
        streaks.put("early_bird", 0); //try to do activities too early in the day
        streaks.put("talkative", 0); //try to interact with non-POIs
        streaks.put("secretive", 0); //interact with the secret

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
                treeEvent();
                break;
            case "chest":
                chestEvent();
                break;
            case "piazza":
                piazzaEvent(args);
                break;
            case "comp_sci":
                compSciEvent(args);
                break;
            case "rch":
                ronCookEvent(args);
                break;
            case "accomodation":
                accomEvent(args);
                break;
            case "flowers":
                flowersEvent(args);
                break;
            case "town":
                busStopEvent(args);
                break;
            case "shop":
                shopEvent(args);
                break;
            case "gym":
                gymEvent(args);
                break;
            case "library":
                libraryEvent(args);
                break;
            case "east":
                townBusStopEvent(args);
                break;
            case "exit":
                // Should do nothing and just close the dialogue menu
                game.dialogueBox.hide();
                break;
            default:
                objectEvent(eventKey);
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
    public void treeEvent() {
        game.dialogueBox.hideSelectBox();
        game.dialogueBox.setText("The tree doesn't say anything back.");
        streaks.put("talkative", streaks.getOrDefault("talkative", 0) + 1);
    }

    /**
     * Sets the text when interacting with the secret area
     */
    public void chestEvent() {
        game.dialogueBox.hideSelectBox();
        game.dialogueBox.setText("Wow! This chest is full of so many magical items! I wonder how they will help you out on your journey! Boy, this is an awfully long piece of text, I wonder if someone is testing something?\n...\n...\n...\nHow cool!");
        streaks.put("talkative", streaks.getOrDefault("talkative", 0) + 1);


    }

    /**
     * Sets the text when talking to an object without a dedicated function
     */
    public void objectEvent(String object) {
        game.dialogueBox.hideSelectBox();
        game.dialogueBox.setText("This is a " +  object + "!");
        streaks.put("talkative", streaks.getOrDefault("talkative", 0) + 1);
    }

    /**
     * Lets the player meet friends at Ron Cooke Hub for x num of hours, decreases the player's energy and increments the
     * game time.
     *
     * @param args Arguments to be passed, should contain the hours the player wants to meet friends for. E.g. ["piazza", "1"]
     */
    public void ronCookEvent(String[] args) {
        if (game.getSeconds() > 8*60) {
            int energyCost = activityEnergies.get("meet_friends");
            // increase player's meeting friends streak
            streaks.put("meet_friends", streaks.getOrDefault("meet_friends", 0) + 1);
            // If the player is too tired to meet friends
            if (game.getEnergy() < energyCost) {
                game.dialogueBox.setText("You are too tired to meet your friends right now!");
                streaks.put("determined", streaks.getOrDefault("determined", 0) + 1);

            } else if (args.length == 1) {
                // Ask the player to chat about something (makes no difference)
                String[] topics = randomTopics(3);
                game.dialogueBox.setText("What do you want to chat about?");
                game.dialogueBox.getSelectBox().setOptions(topics, new String[]{"rch-"+topics[0], "rch-"+topics[1], "rch-"+topics[2]});
            } else {
                // Say that the player chatted about this topic for 1-3 hours
                // RNG factor adds a slight difficulty (may consume too much energy to study)
                int hours = ThreadLocalRandom.current().nextInt(1, 4);
                game.dialogueBox.setText(String.format("You talked about %s for %d hours!", args[1].toLowerCase(), hours));
                game.decreaseEnergy(energyCost * hours);
                game.passTime(hours * 60); // in seconds
                game.addRecreationalHours(hours);
            }
        } else {
            game.dialogueBox.setText("It's too early in the morning to meet your friends, go to bed!");
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
    public void compSciEvent(String[] args) {
        if (game.getSeconds() > 8*60) {
            int energyCost = activityEnergies.get("studying");
            // If the player is too tired for any studying:
            if (game.getEnergy() < energyCost) {
                game.dialogueBox.hideSelectBox();
                game.dialogueBox.setText("You are too tired to study right now!");
                streaks.put("determined", streaks.getOrDefault("determined", 0) + 1);
            } else if (args.length == 1) {
                // If the player has already used their catchup and studied that day, they can't study again
                if ((catchup_used && daily_study == 1) || catchup_used){
                    game.dialogueBox.hideSelectBox();
                    game.dialogueBox.setText("You have already studied today!");
                }else{
                    // If the player has not yet chosen how many hours, ask
                    game.dialogueBox.setText("Study for how long?");
                    game.dialogueBox.getSelectBox().setOptions(new String[]{"1 Hour (10)", "2 Hours (20)", "3 Hours (30)"}, new String[]{"comp_sci-1", "comp_sci-2", "comp_sci-3"});
                }
            } else {
                int hours = Integer.parseInt(args[1]);
                // If the player does not have enough energy for the selected hours
                if (game.getEnergy() < hours*energyCost) {
                    game.dialogueBox.setText("You don't have the energy to study for this long!");
                } else {
                    // If they do have the energy to study
                    game.dialogueBox.setText(String.format("You studied for %s hours!\nYou lost %d energy", args[1], hours*energyCost));
                    game.decreaseEnergy(energyCost * hours);
                    game.addStudyHours(hours);
                    game.addStudyTimes();
                    daily_study++;
                    if (daily_study > 1){
                        catchup_used = GameScreen.useCatchup(catchup_used);
                    }
                    game.passTime(hours * 60); // in seconds
                }
            }
        } else {
            game.dialogueBox.setText("It's too early in the morning to study, go to bed!");
            streaks.put("early_bird", streaks.getOrDefault("early_bird", 0) + 1);
        }
    }


    /**
     * The event to be run when the player interacts with the Piazza
     * Gives the player the choice to eat breakfast, lunch or dinner depending on the time of day
     * @param args
     */
    public void piazzaEvent(String[] args) {
        // increase player's eating at ronCooke streak
        streaks.put("eating", streaks.getOrDefault("eating", 0) + 1);
        if (game.getSeconds() > 8*60) {
            int energyCost = activityEnergies.get("eating");
            if (game.getEnergy() < energyCost) {
                game.dialogueBox.setText("You are too tired to eat right now!");
                streaks.put("determined", streaks.getOrDefault("determined", 0) + 1);
            } else {
                game.dialogueBox.setText(String.format("You took an hour to eat %s at the Piazza!\nYou lost %d energy!", game.getMeal(), energyCost));
                game.decreaseEnergy(energyCost);
                game.passTime(60); // in seconds
            }
        } else {
            game.dialogueBox.setText("It's too early in the morning to eat food, go to bed!");
            streaks.put("early_bird", streaks.getOrDefault("early_bird", 0) + 1);
        }

    }

    /**
     * The event to be run when interacting with the flowerbed bench
     * Gives the player the option to study for 1, 2 or 3 hours
     * @param args
     */
    public void flowersEvent(String[] args) {
        if (game.getSeconds() > 8*60) {
            int energyCost = activityEnergies.get("flowers");
            // increase player's smelling flowers streak
            streaks.put("flowers", streaks.getOrDefault("flowers", 0) + 1);
            // If the player is too tired to smell the flowers
            if (game.getEnergy() < energyCost) {
                game.dialogueBox.hideSelectBox();
                game.dialogueBox.setText("You are too tired to smell the flowers right now!");
                streaks.put("determined", streaks.getOrDefault("determined", 0) + 1);
            } else if (args.length == 1) {
                // If the player has not yet chosen how many hours, ask
                game.dialogueBox.setText("Smell the flowers for how long?");
                game.dialogueBox.getSelectBox().setOptions(new String[]{"1 Hour (10)", "2 Hours (20)", "3 Hours (30)"}, new String[]{"flowers-1", "flowers-2", "flowers-3"});
            } else {
                int hours = Integer.parseInt(args[1]);
                // If the player does not have enough energy for the selected hours
                if (game.getEnergy() < hours*energyCost) {
                    game.dialogueBox.setText("What if you fell asleep? You don't have the energy!");
                } else {
                    // If they do have the energy to smell the flowers
                    game.dialogueBox.setText(String.format("You smelled the flowers for %s hours!\nYou lost %d energy", args[1], hours*energyCost));
                    game.decreaseEnergy(energyCost * hours);
                    game.passTime(hours * 60); // in seconds
                }
            }
        } else {
            game.dialogueBox.setText("It's too early in the morning to smell the flowers, go to bed!");
            streaks.put("early_bird", streaks.getOrDefault("early_bird", 0) + 1);
        }
    }

    /**
     * The event to be run when interacting with the bus stop to town
     * @param args
     */
    public void busStopEvent(String[] args) throws InterruptedException {
        if (game.getSeconds() > 8*60) {
            //Still need an energy cost to prevent player from going into town when they have no energy
            int energyCost = activityEnergies.get("town");
            // If the player is too tired for any travelling:
            if (game.getEnergy() < energyCost) {
                game.dialogueBox.hideSelectBox();
                game.dialogueBox.setText("You are too tired to get the bus right now!");
                streaks.put("determined", streaks.getOrDefault("determined", 0) + 1);
            } else if (args.length == 1) {
                // Player goes into town for undefined amount of time so no need to ask
                // If the player does not have enough energy for at least one hour
                if (game.getEnergy() < energyCost) {
                    game.dialogueBox.setText("You don't have the energy to go into town right now!");
                } else {
                    // If they do have the energy to go into town
                    HustleGame.setMap();
                    game.dialogueBox.setText("You got the bus into town.");
                }
            }
        } else {
            game.dialogueBox.setText("It's too early in the morning to go into town, there are no buses yet!");
            streaks.put("early_bird", streaks.getOrDefault("early_bird", 0) + 1);
        }
    }

    /**
     * The event to be run when interacting with the shop
     * Gives the player the option to buy food for 1, 2 or 3 hours
     * @param args
     */
    public void shopEvent(String[] args) {
        if (game.getSeconds() > 8*60) {
            int energyCost = activityEnergies.get("shop");
            // increase player's buying food streak
            streaks.put("shop", streaks.getOrDefault("shop", 0) + 1);
            // If the player is too tired for any travelling:
            if (game.getEnergy() < energyCost) {
                game.dialogueBox.hideSelectBox();
                game.dialogueBox.setText("You are too tired to buy food right now!");
                streaks.put("determined", streaks.getOrDefault("determined", 0) + 1);
            } else if (args.length == 1) {
                // If the player has not yet chosen how many hours, ask
                game.dialogueBox.setText("Buy food for how long?");
                game.dialogueBox.getSelectBox().setOptions(new String[]{"1 Hour (10)", "2 Hours (20)", "3 Hours (30)"}, new String[]{"shop-1", "shop-2", "shop-3"});
            } else {
                int hours = Integer.parseInt(args[1]);
                // If the player does not have enough energy for the selected hours
                if (game.getEnergy() < hours*energyCost) {
                    game.dialogueBox.setText("You don't have the energy to buy food right now!");
                } else {
                    // If they do have the energy to buy  food
                    game.dialogueBox.setText(String.format("You spent %s hours buying then eating food.\nYou lost %d energy", args[1], hours*energyCost));
                    game.decreaseEnergy(energyCost * hours);
                    game.passTime(hours * 60); // in seconds
                }
            }
        } else {
            game.dialogueBox.setText("It's too early to buy food, the shop's not open yet!");
            streaks.put("early_bird", streaks.getOrDefault("early_bird", 0) + 1);
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
                    daily_study = 0;
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
    public void gymEvent(String[] args) {
        if (game.getSeconds() > 8*60) {
            int energyCost = activityEnergies.get("gym");
            // If the player is too tired for any travelling:
            if (game.getEnergy() < energyCost) {
                game.dialogueBox.hideSelectBox();
                game.dialogueBox.setText("You are too tired to go to the gym right now!");
                streaks.put("determined", streaks.getOrDefault("determined", 0) + 1);
            } else if (args.length == 1) {
                // If the player has not yet chosen how many hours, ask
                game.dialogueBox.setText("Work out for how long?");
                game.dialogueBox.getSelectBox().setOptions(new String[]{"2 Hours (20)", "3 Hours (30)", "4 Hours (40)"}, new String[]{"gym-2", "gym-3", "gym-4"});
            } else {
                int hours = Integer.parseInt(args[1]);
                // If the player does not have enough energy for the selected hours
                if (game.getEnergy() < hours*energyCost) {
                    game.dialogueBox.setText("You don't have the energy to go to work out right now! Head back to east!");
                } else {
                    // If they do have the energy to buy  food
                    game.dialogueBox.setText(String.format("You spent %s hours working out at the gym.\nYou lost %d energy", args[1], hours*energyCost));
                    game.decreaseEnergy(energyCost * hours);
                    game.passTime(hours * 60); // in seconds
                }
            }
        } else {
            game.dialogueBox.setText("It's too early to work out, the gym's not open yet!");
            streaks.put("early_bird", streaks.getOrDefault("early_bird", 0) + 1);
        }
    }

    /**
     * The event to be run when interacting with the library
     * Gives the player the option to study for 2, 3 or 4 hours - must go into town to study longer
     * @param args
     */
    public void libraryEvent(String[] args) {
        if (game.getSeconds() > 8*60) {
            int energyCost = activityEnergies.get("library");
            // If the player is too tired for any studying:
            if (game.getEnergy() < energyCost) {
                game.dialogueBox.hideSelectBox();
                game.dialogueBox.setText("You are too tired to study at the lirbary right now!");
                streaks.put("determined", streaks.getOrDefault("determined", 0) + 1);
            } else if (args.length == 1) {
                // If the player has already used their catchup and studied that day, they can't study again
                if ((catchup_used && daily_study == 1) || catchup_used){
                    game.dialogueBox.hideSelectBox();
                    game.dialogueBox.setText("You have already studied today!");
                }else{
                    // If the player has not yet chosen how many hours, ask
                    game.dialogueBox.setText("Study for how long?");
                    game.dialogueBox.getSelectBox().setOptions(new String[]{"2 Hours (20)", "3 Hours (30)", "4 Hours (40)"}, new String[]{"library-2", "library-3", "library-4"});
                }
            } else {
                int hours = Integer.parseInt(args[1]);
                // If the player does not have enough energy for the selected hours
                if (game.getEnergy() < hours*energyCost) {
                    game.dialogueBox.setText("You don't have the energy to study for this long! Head back to east!");
                } else {
                    // If they do have the energy to study
                    game.dialogueBox.setText(String.format("You studied for %s hours!\nYou lost %d energy", args[1], hours*energyCost));
                    game.decreaseEnergy(energyCost * hours);
                    game.addStudyHours(hours);
                    game.addStudyTimes();
                    daily_study++;
                    if (daily_study > 1){
                        catchup_used = GameScreen.useCatchup(catchup_used);
                    }
                    game.passTime(hours * 60); // in seconds
                }
            }
        } else {
            game.dialogueBox.setText("It's too early in the morning to study, go to bed!");
            streaks.put("early_bird", streaks.getOrDefault("early_bird", 0) + 1);
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
