import xlrd
import datetime
import xlsxwriter


def getCellStatuses(cellColors):
    return [getStatusFromCellColor(cellColor) for cellColor in cellColors]


def getStatusFromCellColor(cellColor):
    if cellColor == (255, 0, 0):
        return "FAILED"

    return "COMPLETED"


def getCellColors(workbook, sheet, columnNumber):
    cellColors = []
    #need to get each cell in column
    for rowNumber in range(sheet.nrows):
        xfx = sheet.cell_xf_index(rowNumber, columnNumber)
        xf = workbook.xf_list[xfx]
        bgx = xf.background.pattern_colour_index
        pattern_colour = workbook.colour_map[bgx]

        cellColors.append(pattern_colour)
        # print(pattern_colour)
    return cellColors[1:]


def isDate(date_values):
    if (date_values[0] != 0 and date_values[1] != 0 and date_values[2] != 0):
        return True
    return False


def getExerciseActivityFromRows(weightRows, repRows, notesRows, statuses):
    exerciseActivity = {}

    # print("weightRows " + str(len(weightRows)))
    # print("repRows " + str(len(repRows)))
    # print("statuses " + str(len(statuses)))
    currentExercise = None
    for index, row in enumerate(weightRows):
        if row == '':
            currentExercise = None
        elif currentExercise == None:
            currentExercise = row.lower().title()
        elif row == 'weight':
            continue
        else:
            rep = repRows[index]
            notes = notesRows[index]
            status = statuses[index]

            if currentExercise in exerciseActivity:
                exerciseActivity[currentExercise].append(
                    (row, rep, notes, status))
            else:
                exerciseActivity[currentExercise] = [(row, rep, notes, status)]

    # print(exerciseActivity
    return exerciseActivity


file_path = "tempworkouts.xls"
workbook = xlrd.open_workbook(file_path, formatting_info=True)
sheet = workbook.sheet_by_index(0)

# print(sheet.nrows)
workouts = {}
exerciseActivities = []
for columnNumber in range(sheet.ncols):

    header = sheet.cell_value(0, columnNumber)
    if sheet.cell(0, columnNumber).ctype == xlrd.XL_CELL_DATE:

        date_values = xlrd.xldate_as_tuple(header, workbook.datemode)

        if isDate(date_values):
            #time is in the cell to the right of the date
            time_values = xlrd.xldate_as_tuple(
                sheet.cell_value(0, columnNumber + 1), workbook.datemode)

            date_time = datetime.datetime(
                *[*date_values[:3], *time_values[3:]])

            #get all rows from this col
            weightRows = sheet.col_values(columnNumber, 1)
            repRows = sheet.col_values(columnNumber + 1, 1)

            # print(repRows)
            # print("\n")
            notesRows = sheet.col_values(columnNumber + 2, 1)

            #need to get the colors for the cells in the rep rows
            cellColors = getCellColors(workbook, sheet, columnNumber + 1)

            statuses = getCellStatuses(cellColors)

            # print(statuses)

            exerciseActivity = getExerciseActivityFromRows(
                weightRows, repRows, notesRows, statuses)

            exerciseActivities.append(exerciseActivity)

            workouts[date_time] = exerciseActivity
            # if "" in exerciseActivity.keys():
            # if True:
            #     print(date_time)
            #     print(list(exerciseActivity.keys()))
            #     print("\n")


def writeToFile(workouts):
    workbook = xlsxwriter.Workbook("newWorkouts.xlsx")
    worksheet = workbook.add_worksheet()

    currentRow = 0
    for workout in workouts:
        #write date_time

        dateFormat = workbook.add_format({'num_format': 'dd/mm/yy hh:mm'})

        worksheet.write(currentRow, 0, workout, dateFormat)
        currentRow += 1

        #write exercises
        for exercise in workouts[workout]:

            #write exercise name
            worksheet.write(currentRow, 1, exercise)
            currentRow += 1

            for index, exerciseActivity in enumerate(
                    workouts[workout][exercise]):

                # print(exerciseActivity)

                #write weight
                worksheet.write(currentRow, 2 + index, exerciseActivity[0])

                #write reps
                worksheet.write(currentRow + 1, 2 + index, exerciseActivity[1])

                #write statuses
                worksheet.write(currentRow + 2, 2 + index, exerciseActivity[3])

                #write notes
                worksheet.write(currentRow + 3, 2 + index, exerciseActivity[2])

            currentRow += 4
    workbook.close()


writeToFile(workouts)

# for workout in workouts:
#     print(workout)
#     print(workouts[workout])

# for exerciseActivity in exerciseActivities:
#     print(list(exerciseActivity.keys()))
#     print("\n")

exercises = set()
for exerciseActivity in exerciseActivities:
    for exercise in exerciseActivity.keys():
        exercises.add(exercise)

for exercise in exercises:
    print(exercise)
