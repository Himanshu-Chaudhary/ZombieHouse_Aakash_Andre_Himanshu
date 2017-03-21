# Read Me

## Introduction
This Zombie House game is based partially on code written by Atle Olson, Jeffrey McCall, and Ben Matthews in Spring 2016 at UNM, and the work of the team of Nick Schrandt, Hector Carillo, and Sarah Salmonson, Fall 2016 at UNM. Further changes and restructuring were made upon this work by Andre' Green, Himanshu Chaudhary, and Aakash Basnet.

## How to Use the Program:
Entry Point: GameMain.java
Game is launched, advising the player to press (or not press, it flickers on purpose) spacebar.
Controls:
	WASD: Player Movement
	Mouse on screen: Rotation for camera
	Shift: Sprint (Stamina limited by player health)
	Space: Attack zombie
	O: Simulate player death
	P: Simulate level completion
---

## GamePlay
Some number of zombies are spawned in a large maze. They will randomly wander about until the player comes into smell range, and then chase the player, attacking if they get close enough. The player can attack zombies if so desired, or sprint away from them. The player's goal each time is to reach the exit. Levels continue indefinetly, getting increasingly difficult.
---

## Zombie pathfinding, movement and AI:
Random walk: Change direction every 2s.
Line walk: Change direction every 2s, if colliding with wall.

##### Pathfinding:
Used A* (see pathfinding source code). 

##### Master Zombie
Has extended pathfinding capabilities, searching for the player from anywhere on the map.

## Player Movement and Controls
Player movement is handled within the player class and recieves input from InputHandler.

## Procedural Map Generation: 
Procedural map generation is performed based on a series of algorithms that take in width, height and difficulty.

##### setup:
Firstly, a map is created that is 1/4th the size of the given map dimensions. this is done because it allows for simple hallway calculation on resize (a room of width 1 becomes a hallway of width 3). A rectangular region for the map is created and

##### Dividing the Space:
The space is divided using a binary splitting function, which takes a rectangle and splits it into 2 smaller rectangles that fill the space of the previous one. This is done such that the split is perpendicular to the smaller side. 

This function is repeated first 3 times to get the 4 region dimensions in the game, and is then repeated for each region until all rooms are at least less than size 3 (12 in full size game space). after this hallways are split off of the current rooms. the room at coordinate [0, 0] is split into a hallway if it is not already one.

##### Connecting Rooms Hallways and Regions:
Rooms and hallways are connected such that all rooms and hallways are connected and every hallway has at least 2 doors. First, all rooms and hallways for a region are connected in a non-directed graph with no cycles. then hallways with less than 2 connections are connected to a random neighbor. after all the rooms and hallways have been connected for each region, a single connection is inserted from each region to the next region in the path (1 -> 2 -> 3 -> 4)

##### Finishing:
the rooms and hallways are resized to full size (x4) and a 2d Tile array is created. for each room the tile is set to the region type unless it is the lower or right-hand edge, in which case it is a wall.
the exit is added to a random border room in region 4 and obstacles are created based on the difficulty setting on tiles with odd-numbered-coordinates (to prevent unreachable areas)

This tile array is returned as the product of the procedural generation

##### Meshes and 3D rendering:
Zombie Meshes were imported using the jfx3dObjimporter by InteractiveMesh.org.
The importer returned an array of Nodes (Meshviews to be precise) whose meshes were pulled and added to a map so that meshes did not need to be imported multiple times.

##### Sounds:
Sounds were implemented using a SoundManager class which loaded all of the sounds in the game and handled playing those sounds. AudioClips were used for the short game sounds and a MediaPlayer was used for the “long” Mp3 files. 

All sound clips used for this project were either created using FL-studio (music creation software) or taken from Freesound.org. Music for the project was taken from Purple-Planet Music
---

## Sources:

##### Texture sources:
http://rotane.deviantart.com/art/tileable-texture-wooden-floor-18627796
http://darkrose42-stock.deviantart.com/art/Grunge-texture-50620777
Multiple from 
	http://www.cadhatch.com/seamless-brick-textures/4588167765
	http://www.cadhatch.com/seamless-stone-textures/4588167766
	http://www.cadhatch.com/seamless-metal-textures/4588167772
	http://www.cadhatch.com/seamless-road-paving-textures/4588167770

[ Variants were made, mixing and matching textures and patterns in GIMP. ]

##### 3D Model sources:
* Model Importer - jfx3dObjimporter by InteractiveMesh.org
* Low Poly Character Rigged - http://tf3dm.com/3d-model/low-poly-character-rigged-47686.html
	[ Variants were made on this model. ]
* Wall & pillar/obstacle meshes were created from scratch.

##### Sound sources:
* http://freesound.org/
* http://www.pacdv.com/sounds/voices-1.html
* http://soundbible.com/

---
## Known Bugs / Missing Features
* There is no death animation for the past player.
* There is no definitive 'end' for the game nor limit on lives expended per level.
* The camera may at time clip into the wall if the player backs up too close to the wall.
