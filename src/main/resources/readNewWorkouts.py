import xlrd
import datetime

# DELETE FROM EXERCISE_SET_ENTITY;
# DELETE FROM EXERCISE_ACTIVITY_ENTITY_SETS;
# DELETE FROM EXERCISE_ACTIVITY_ENTITY;
# DELETE FROM WORKOUT_ENTITY;
# DELETE FROM WORKOUT_ENTITY_EXERCISE_ACTIVITIES;

# INSERT INTO EXERCISE_ACTIVITY_ENTITY (ID, NOTES, EXERCISE_ID)
# VALUES (3, '', SELECT ID FROM EXERCISE_ENTITY WHERE NAME = 'Bicep Curl');

# INSERT INTO EXERCISE_ACTIVITY_ENTITY_SETS (EXERCISE_ACTIVITY_ENTITY_ID, SETS_ID)
# VALUES (1, 0);

# INSERT INTO EXERCISE_ENTITY (INFORMATION, MUSCLE_GROUP, NAME)
# VALUES ('info', 'BICEP', 'Bicep Curl');

# INSERT INTO WORKOUT_ENTITY_EXERCISE_ACTIVITIES (WORKOUT_ENTITYID, EXERCISE_ACTIVITIES_ID)
# VALUES (1, 3);

# INSERT INTO WORKOUT_ENTITY (ID, NAME, NOTES, PERFORMED_AT_TIMESTAMP_UTC, WORKOUT_TYPE)
# VALUES (10, 'Workout Name', 'Notes', CURRENT_TIMESTAMP, 2);

# INSERT INTO EXERCISE_SET_ENTITY (SET_TYPE, ID, NUMBER_OF_REPS, STATUS , WEIGHT , WEIGHT_KG)
# VALUES (1, 1, 3, 0, null, 1)


def writeToFile(queries):
    print(len(queries))
    file = open("oldWorkouts.sql", "w")
    for query in queries:
        file.write("%s\n" % query)

    file.close()


def getWorkoutType(workout):
    exercises = [exercise.lower() for exercise in workout.keys()]

    pullExercises = ["deadlift", "pull up", "chin up", "bent over row"]
    pushExercises = [
        "bench press", "dumbbell floor press", "reverse bench press"
    ]
    legExercises = ["squat", "hip thrust", "goblet squat"]
    if any(pullExercise in exercises for pullExercise in pullExercises):
        return "PULL"
    if any(pushExercise in exercises for pushExercise in pushExercises):
        return "PUSH"
    if any(legExercise in exercises for legExercise in legExercises):
        return "LEGS"

    # print("UNKNOWN")
    # print(workout)
    # print("UNKNOWN")
    return "UNKNOWN"


def is_number(s):
    try:
        float(s)
        return True
    except ValueError:
        return False


def generateExerciseQueries(exercises):
    for index, exercise in enumerate(exercises):
        addExerciseQuery = "INSERT INTO EXERCISE_ENTITY (INFORMATION, MUSCLE_GROUP, NAME) " + \
        "VALUES ('{}', 'BICEP', '{}');".format(exercise, exercise)

        print(addExerciseQuery)


def generateWorkoutQueries(workouts):
    queries = []
    for workoutIndex, workout in enumerate(workouts):
        print(workout)

        workoutType = getWorkoutType(workouts[workout])

        addWorkoutQuery = "INSERT INTO WORKOUT_ENTITY (ID, NOTES, PERFORMED_AT_TIMESTAMP_UTC, WORKOUT_TYPE) " + \
        "VALUES ({}, 'Notes', '{}', '{}');".format(workoutIndex + 1, workout, workoutType)

        print(addWorkoutQuery)
        queries.append(addWorkoutQuery)

        exerciseActivity = workouts[workout]

        # print(exerciseActivity)

        for exerciseActivityIndex, exercise in enumerate(exerciseActivity):

            calculatedExerciseActivityId = (
                100 * (workoutIndex + 1)) + exerciseActivityIndex

            addExerciseActivityQuery = "INSERT INTO EXERCISE_ACTIVITY_ENTITY (ID, NOTES, EXERCISE_ID, WORKOUT_ID) " + \
            "VALUES ({}, '', (SELECT ID FROM EXERCISE_ENTITY WHERE NAME = '{}'), {});".format(calculatedExerciseActivityId, exercise, workoutIndex + 1)

            exerciseSets = exerciseActivity[exercise]

            print(addExerciseActivityQuery)
            queries.append(addExerciseActivityQuery)

            for setIndex, set in enumerate(exerciseSets):

                weightKg = "null"
                weight = "null"

                #weight value before we know if its weighted or non weighted
                tempWeight = set[0]

                # print(tempWeight)

                numberOfReps = set[1]

                #1 for non weighted, 2 for weighted
                setType = "WeightedSet"

                if "kg" in str(tempWeight):
                    weightKg = tempWeight.replace("kg", "")

                #TODO this will change to timedset
                elif "second" in str(numberOfReps):
                    weight = "'" + numberOfReps + "'"
                    numberOfReps = 1
                    setType = "NonWeightedSet"

                elif is_number(tempWeight) and numberOfReps == '':
                    weight = "'body'"
                    numberOfReps = tempWeight
                    setType = "NonWeightedSet"

                elif is_number(tempWeight) and numberOfReps != '':
                    weight = tempWeight

                else:
                    weight = "'" + tempWeight + "'"
                    setType = "NonWeightedSet"

                calculatedSetId = (calculatedExerciseActivityId *
                                   10) + setIndex

                status = 0 if set[3] == "COMPLETED" else 1

                addSetQuery = "INSERT INTO EXERCISE_SET_ENTITY (SET_TYPE, ID, NUMBER_OF_REPS, STATUS , WEIGHT , WEIGHT_KG, EXERCISE_ACTIVITY_ID) " + \
                    "VALUES ('{}', {}, {}, {}, {}, {}, {});".format(setType, calculatedSetId, numberOfReps, status, weight, weightKg, calculatedExerciseActivityId)

                addExerciseActivityEntitySetsQuery = "INSERT INTO EXERCISE_ACTIVITY_ENTITY_SETS (EXERCISE_ACTIVITY_ENTITY_ID, SETS_ID) " + \
                    "VALUES ({}, {});".format(calculatedExerciseActivityId, calculatedSetId)

                print(addSetQuery)
                queries.append(addSetQuery)
                # print(addExerciseActivityEntitySetsQuery)
                # queries.append(addExerciseActivityEntitySetsQuery)


            addWorkoutEntityExerciseActivityQuery = "INSERT INTO WORKOUT_ENTITY_EXERCISE_ACTIVITIES (WORKOUT_ENTITY_ID, EXERCISE_ACTIVITIES_ID) " + \
                " VALUES ({}, {});".format(workoutIndex + 1, calculatedExerciseActivityId)

            # print(addWorkoutEntityExerciseActivityQuery)
            # queries.append(addWorkoutEntityExerciseActivityQuery)

        if workoutIndex == 3:
            pass

    writeToFile(queries)

    for query in queries:
        if "UNKNOWN" in query:
            print(query)


def isDate(date):
    if date != "":
        return True
    return False


file_path = "newWorkouts.xlsx"
workbook = xlrd.open_workbook(file_path)
sheet = workbook.sheet_by_index(0)

workouts = {}

for row in range(sheet.nrows):
    headerCol = sheet.cell_value(row, 0)

    if isDate(headerCol):
        workout = {}
        currentRow = row
        workoutDate = xlrd.xldate_as_datetime(headerCol, workbook.datemode)

        # print(workoutDate)

        #get each exercise
        hasExercise = True

        exercise = {}
        while hasExercise:
            currentRow += 1

            exerciseName = sheet.cell_value(currentRow, 1)

            # print(exerciseName)
            # weightRow = sheet.cell_value(currentRow + 1, 2)
            weightRow = [
                weight.value for weight in sheet.row(currentRow + 1)[2:]
            ]
            # print(weightRow)

            # repRow = sheet.cell_value(currentRow + 2, 2)
            repRow = [rep.value for rep in sheet.row(currentRow + 2)][2:]
            # print(repRow)

            statusRow = [status.value
                         for status in sheet.row(currentRow + 3)][2:]

            if currentRow < sheet.nrows - 4:
                notesRow = [
                    notes.value for notes in sheet.row(currentRow + 4)
                ][2:]
                # print(notesRow)

            exercise = [
                set for set in (zip(weightRow, repRow, notesRow, statusRow))
                if set[0] != "" or set[1] != ""
            ]

            workout[exerciseName] = exercise

            if currentRow < sheet.nrows - 5:
                nextHeaderCell = sheet.cell_value(currentRow + 5, 0)

                if isDate(nextHeaderCell):
                    hasExercise = False

            else:
                break

            currentRow += 4

    workouts[workoutDate] = workout

# for workout in workouts.keys():
#     print(workout)
#     # print(workouts[workout])
#     for exercise in workouts[workout]:
#         print(exercise)
#         # print(exercise + " " + str(workouts[workout][exercise]))
#     print("\n")

exercises = []
for workout in workouts.keys():
    exercises += (list(workouts[workout].keys()))

#get unique exercises
# print(set(exercises))
# generateExerciseQueries(set(exercises))

# lastWorkout = list(workouts.items())[-1]
# print(lastWorkout)
generateWorkoutQueries(workouts)