"""Моделі предметної області «Каталог фільмів» (Варіант 8)."""

from __future__ import annotations

from typing import Protocol


class Recommendable(Protocol):
    """Протокол об'єктів, які мають базові поля каталогу та бал рекомендації."""

    @property
    def title(self) -> str:
        """Повертає назву фільму."""
        ...

    @property
    def director(self) -> str:
        """Повертає режисера фільму."""
        ...

    @property
    def release_year(self) -> int:
        """Повертає рік випуску фільму."""
        ...

    @property
    def duration_minutes(self) -> int:
        """Повертає тривалість фільму у хвилинах."""
        ...

    @property
    def rating(self) -> float:
        """Повертає оцінку фільму."""
        ...

    def recommendation_score(self) -> float:
        """Обчислює бал рекомендації фільму."""
        ...


class Movie:
    """Базовий клас фільму з інкапсульованими властивостями та перевіркою інваріантів."""

    def __init__(
        self,
        title: str,
        director: str,
        release_year: int,
        duration_minutes: int,
        rating: float,
    ) -> None:
        """Ініціалізує об'єкт фільму та перевіряє вхідні значення."""
        clean_title = title.strip()
        clean_director = director.strip()
        if not clean_title:
            raise ValueError("Назва фільму не може бути порожньою")
        if not clean_director:
            raise ValueError("Ім'я режисера не може бути порожнім")

        self._title = clean_title
        self._director = clean_director
        self.release_year = release_year
        self.duration_minutes = duration_minutes
        self.rating = rating

    @property
    def title(self) -> str:
        """Повертає назву фільму."""
        return self._title

    @property
    def director(self) -> str:
        """Повертає ім'я режисера."""
        return self._director

    @property
    def release_year(self) -> int:
        """Повертає рік випуску фільму."""
        return self._release_year

    @release_year.setter
    def release_year(self, value: int) -> None:
        """Встановлює рік випуску в діапазоні 1888..2100."""
        if isinstance(value, bool) or not isinstance(value, int):
            raise ValueError("Рік випуску має бути цілим числом")
        if not (1888 <= value <= 2100):
            raise ValueError("Рік випуску має бути в межах 1888..2100")
        self._release_year = value

    @property
    def duration_minutes(self) -> int:
        """Повертає тривалість фільму у хвилинах."""
        return self._duration_minutes

    @duration_minutes.setter
    def duration_minutes(self, value: int) -> None:
        """Встановлює додатну тривалість фільму у хвилинах."""
        if isinstance(value, bool) or not isinstance(value, int):
            raise ValueError("Тривалість має бути цілим числом")
        if value <= 0:
            raise ValueError("Тривалість має бути більшою за 0")
        self._duration_minutes = value

    @property
    def rating(self) -> float:
        """Повертає оцінку фільму."""
        return self._rating

    @rating.setter
    def rating(self, value: float) -> None:
        """Встановлює оцінку фільму в діапазоні 0.0..10.0."""
        if isinstance(value, bool) or not isinstance(value, int | float):
            raise ValueError("Оцінка має бути числом")
        numeric = float(value)
        if not (0.0 <= numeric <= 10.0):
            raise ValueError("Оцінка має бути в межах 0.0..10.0")
        self._rating = numeric

    def recommendation_score(self) -> float:
        """Обчислює бал рекомендації фільму (перевизначається у підтипах)."""
        raise NotImplementedError("Підтипи Movie повинні реалізувати recommendation_score()")


class FeatureFilm(Movie):
    """Художній фільм із формулою балу рекомендації за оцінкою та тривалістю."""

    def recommendation_score(self) -> float:
        """Повертає бал рекомендації для художнього фільму."""
        return round(self.rating * 10.0 + self.duration_minutes * 0.05, 4)


class Documentary(Movie):
    """Документальний фільм із формулою балу рекомендації за оцінкою та тривалістю."""

    def recommendation_score(self) -> float:
        """Повертає бал рекомендації для документального фільму."""
        return round(self.rating * 12.0 + self.duration_minutes * 0.02, 4)
