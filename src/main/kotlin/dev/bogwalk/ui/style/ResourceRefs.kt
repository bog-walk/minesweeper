package dev.bogwalk.ui.style

import androidx.compose.ui.unit.dp

const val WINDOW_TITLE = "Minesweeper"

// General dimensions
val tinyPadding = 2.dp
val smallPadding = 10.dp
val windowPadding = 15.dp
val headerHeight = 30.dp
const val BEVEL_STROKE_SM = 2.2f
const val BEVEL_STROKE_LR = 3f
val dialogSize = 350.dp

val staticWidth = 46.dp
val staticHeight = 157.dp

// MenuBar
const val OPTIONS_MENU = "Options"
const val GAME_MENU = "New Game"
const val RULES_MENU = "Rules"

// NewGameDialog
const val HEADER1 = "Height"
const val HEADER2 = "Width"
const val HEADER3 = "Mines"
const val LEVEL_CUSTOM = "CUSTOM"
const val START_GAME = "NEW GAME"
const val HEIGHT_ERROR_TEXT = "Height must be between 9 and 30"
const val WIDTH_ERROR_TEXT = "Width must be between 9 and 50"
const val MINES_ERROR_TEXT = "Mines must be between 5 and"

// RulesDialog
const val FACE_CLICK = "Start a new game"
const val LEFT_SCREEN = "Flags available"
const val RIGHT_SCREEN = "Seconds passed"
const val LEFT_CLICK = "Reveal"
const val RIGHT_CLICK = "Flag/Unflag"
const val LEFT_CLICK_DESCRIPTION = "Left mouse click animation"
const val RIGHT_CLICK_DESCRIPTION = "Right mouse click animation"

// TimeExceededDialog
const val TIME_OUT = "Time has run out!"

// ResetButton
const val RESET_DEFAULT_ICON = "face_default.svg"
const val RESET_DEFAULT_DESCRIPTION = "smiling face"
const val RESET_LOST_ICON = "face_lost.svg"
const val RESET_LOST_DESCRIPTION = "sad face"
const val RESET_WON_ICON = "face_won.svg"
const val RESET_WON_DESCRIPTION = "happy face with sunglasses"

// DigitalScreen
const val DIGITAL_TEST_TAG = "digital screen"

// MSCell
val cellSize = 20.dp
const val FLAG_ICON = "flag.svg"
const val FLAG_DESCRIPTION = "tiny red flag"
const val MINE_ICON = "mine.svg"
const val MINE_DESCRIPTION = "tiny mine"
const val MINE_X_ICON = "mine_x.svg"
const val MINE_X_DESCRIPTION = "crossed-out mine"

// MSGrid
const val GRID_TEST_TAG = "grid column"