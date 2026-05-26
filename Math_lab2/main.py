# ИМПОРТ БИБЛИОТЕК
import numpy as np
import matplotlib.pyplot as plt

# ПАРАМЕТРЫ
L = 2  # интервал [0, L]
N_values = [5, 10, 50]  # частичные суммы
x = np.linspace(0, L, 2000)


# ИСХОДНАЯ ФУНКЦИЯ
def f(x):
    """
    Исходная функция:
    f(x) = 3x - 1, x in [0,1]
    f(x) = 0, x in [1,2]
    """
    return np.where((x >= 0) & (x <= 1), 3*x - 1, 0)


# ЧИСЛЕННОЕ ИНТЕГРИРОВАНИЕ
def integrate(func, a=0, b=1, n=10000):
    xs = np.linspace(a, b, n)
    ys = func(xs)
    return np.trapezoid(ys, xs)


# КОЭФФИЦИЕНТЫ ОБЩЕГО РЯДА ФУРЬЕ
def a0():
    return (1 / L) * integrate(lambda x: f(x))


def an(n):
    return (1 / L) * integrate(
        lambda x: f(x) * np.cos(np.pi * n * x / L)
    )


def bn(n):
    return (1 / L) * integrate(
        lambda x: f(x) * np.sin(np.pi * n * x / L)
    )


# ОБЩИЙ ТРИГОНОМЕТРИЧЕСКИЙ РЯД ФУРЬЕ
def fourier_general(x, N):
    result = a0() / 2

    for n in range(1, N + 1):
        result += (
            an(n) * np.cos(np.pi * n * x / L)
            + bn(n) * np.sin(np.pi * n * x / L)
        )

    return result


# РЯД ФУРЬЕ ПО СИНУСАМ
def bn_sin(n):
    return (2 / L) * integrate(
        lambda x: f(x) * np.sin(np.pi * n * x / L)
    )


def fourier_sin(x, N):
    result = 0

    for n in range(1, N + 1):
        result += bn_sin(n) * np.sin(np.pi * n * x / L)

    return result


# РЯД ФУРЬЕ ПО КОСИНУСАМ
def a0_cos():
    return (2 / L) * integrate(lambda x: f(x))


def an_cos(n):
    return (2 / L) * integrate(
        lambda x: f(x) * np.cos(np.pi * n * x / L)
    )


def fourier_cos(x, N):
    result = a0_cos() / 2

    for n in range(1, N + 1):
        result += an_cos(n) * np.cos(np.pi * n * x / L)

    return result


# ВЫВОД КОЭФФИЦИЕНТОВ
print("=" * 50)
print("ОБЩИЙ РЯД ФУРЬЕ")
print("=" * 50)

print(f"a0 = {a0():.6f}")

for n in range(1, 11):
    print(
        f"n={n:2d}: "
        f"a{n} = {an(n): .6f}, "
        f"b{n} = {bn(n): .6f}"
    )

print("\n" + "=" * 50)
print("РЯД ПО СИНУСАМ")
print("=" * 50)

for n in range(1, 11):
    print(f"b{n} = {bn_sin(n): .6f}")

print("\n" + "=" * 50)
print("РЯД ПО КОСИНУСАМ")
print("=" * 50)

print(f"a0 = {a0_cos():.6f}")

for n in range(1, 11):
    print(f"a{n} = {an_cos(n): .6f}")


# ГРАФИКИ
def plot_series(series_func, title):
    plt.figure(figsize=(12, 7))

    # Исходная функция
    plt.plot(
        x,
        f(x),
        linewidth=3,
        label='Исходная функция'
    )

    # Частичные суммы
    for N in N_values:
        plt.plot(
            x,
            series_func(x, N),
            label=f'S{N}'
        )

    plt.title(title, fontsize=14)
    plt.xlabel('x')
    plt.ylabel('y')
    plt.grid(True)
    plt.legend()
    plt.show()


# ПОСТРОЕНИЕ ГРАФИКОВ
plot_series(
    fourier_general,
    'Общий тригонометрический ряд Фурье'
)

plot_series(
    fourier_sin,
    'Ряд Фурье по синусам'
)

plot_series(
    fourier_cos,
    'Ряд Фурье по косинусам'
)