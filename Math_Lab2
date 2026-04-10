# Импорт бибилиотек
import numpy as np # Удобноя и простая работа с числами
import matplotlib.pyplot as plt # Построение графиков

"""
f(x) = 5^x
Пределы интегрирования
a = 0
b = 3
"""

# Искомая функция
def f(x):
    return 5**x


# Функция для вычисления итегральной суммы
def riemann_sum(n, method, a=0, b=3):
    """
    n - число точек разбиения
    method - способ выбора оснащения (
    левые = left,
    правые = right,
    средние = mid,
    случайные точки = random
    )
    a - нижня граница
    b - верхня граница
    """

    dx = (b - a) / n # Шаг разбиения

    # np.linspace(start, stop, n) возвращает массив чисел размерностью n равномерно распределнных между start, stop
    if method == "left":
        x = np.linspace(a, b-dx, n)

    elif method == "right":
        x = np.linspace(a+dx, b, n)

    elif method == "mid":
        x = np.linspace(a+dx/2, b-dx/2, n)

    elif method == "random":
        left_edges = a + np.arange(n)*dx # левая граница каждого подотрезка
        x = left_edges + np.random.rand(n)*dx # случайное смещение внутри подотрезка

    # Обработки ошибки
    else:
        raise ValueError("Unknown method")

    heights = f(x) # Высота прямоугольника
    S = np.sum(heights * dx) # Сложение площадей всех прямоугольнкиов

    return x, heights, dx, S


# Функция для рисования графиков
def draw_graphs(n, method, a=0, b=3):

    x, heights, dx, S = riemann_sum(n, method)

    # точки для гладкого графика функции
    x_func = np.linspace(a, b, 400)

    plt.figure(figsize=(8,5))

    # график функции
    plt.plot(x_func, f(x_func), label="f(x) = 5^x")

    # рисуем прямоугольники
    for i in range(n):

        if method == "left":
            left = a + i*dx

        elif method == "right":
            left = a + i*dx

        elif method == "mid":
            left = a + i*dx

        elif method == "random":
            left = a + i*dx

        rect = plt.Rectangle(
            (left, 0),
            dx,
            heights[i],
            edgecolor='black',
            alpha=0.3
        )

        plt.gca().add_patch(rect)

    plt.title(f"Интегральная сумма Римана ({method}), n={n}\nS ≈ {S:.5f}")

    plt.xlabel("x")
    plt.ylabel("y")

    plt.legend()
    plt.grid(True)

    plt.show()


# Основная часть программы
if __name__ == "__main__":
    draw_graphs(100, "left")
    draw_graphs(150, "right")
    draw_graphs(50, "mid")
    draw_graphs(10, "random")
